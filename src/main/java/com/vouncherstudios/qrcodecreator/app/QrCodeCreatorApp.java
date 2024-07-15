/*
 * MIT License
 *
 * Copyright (c) Vouncher Studios <contact@vouncherstudios.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.vouncherstudios.qrcodecreator.app;

import com.vouncherstudios.qrcodecreator.rate.IpRateLimiter;
import com.vouncherstudios.qrcodecreator.url.UrlQueryBuilder;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QrCodeCreatorApp {
  private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeCreatorApp.class);
  private static final String API_URL = "https://api.qrserver.com/v1/create-qr-code";

  private final ExpiringMap<Integer, byte[]> cache =
      ExpiringMap.builder()
          .maxSize(1000000)
          .expiration(10, TimeUnit.MINUTES)
          .expirationPolicy(ExpirationPolicy.ACCESSED)
          .build();
  private final IpRateLimiter rateLimiter = new IpRateLimiter(10, Duration.ofMinutes(1));
  private final Set<String> exemptIps;
  private final Javalin app;

  public QrCodeCreatorApp(int port, @Nonnull Set<String> exemptIps) {
    this.exemptIps = exemptIps;
    this.app =
        Javalin.create()
            .get(
                "/api/create",
                context -> {
                  String data = context.queryParam("data");
                  if (data == null || data.isEmpty()) {
                    context.status(400).result("Data parameter is required.");
                    return;
                  }

                  String ip = context.ip();
                  if (isAbleToConsume(data, ip)) {
                    fetchAndDisplayQrCode(context, data);
                  } else {
                    context.status(429).result("Rate limit exceeded.");
                  }
                })
            .start(port);
    Runtime.getRuntime().addShutdownHook(new Thread(this.app::stop));
  }

  private boolean isAbleToConsume(@Nonnull String data, @Nonnull String ip) {
    if (this.cache.containsKey(data.hashCode()) || this.exemptIps.contains(ip)) {
      return true;
    }

    return this.rateLimiter.tryConsume(ip);
  }

  private void fetchAndDisplayQrCode(@Nonnull Context context, @Nonnull String data) {
    try {
      int hashCode = data.hashCode();

      byte[] imageData = this.cache.get(hashCode);
      if (imageData == null) {
        UrlQueryBuilder urlQueryBuilder = new UrlQueryBuilder(API_URL);
        urlQueryBuilder.add("data", data);
        String mountedUrl = urlQueryBuilder.toURL();

        URL url = new URL(mountedUrl);
        BufferedImage img = ImageIO.read(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        imageData = baos.toByteArray();

        this.cache.put(hashCode, imageData);
      }

      context.header("Content-Type", "image/png");
      context.result(imageData);
    } catch (Exception e) {
      LOGGER.error("Failed to generate QR Code.", e);
      context.status(500).result("Failed to generate QR Code.");
    }
  }

  @Nonnull
  public Javalin getApp() {
    return this.app;
  }
}
