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

package com.vouncherstudios.qrcodegenerator.app;

import com.vouncherstudios.qrcodegenerator.code.CodeGenerator;
import com.vouncherstudios.qrcodegenerator.parameter.ParameterQuery;
import com.vouncherstudios.qrcodegenerator.rate.IpRateLimiter;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.nayuki.qrcodegen.QrCode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QrCodeGeneratorApp {
  private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeGeneratorApp.class);

  private final ExpiringMap<Integer, byte[]> cache =
      ExpiringMap.builder()
          .maxSize(100000)
          .expiration(10, TimeUnit.MINUTES)
          .expirationPolicy(ExpirationPolicy.ACCESSED)
          .build();
  private final IpRateLimiter rateLimiter;
  private final CodeGenerator codeGenerator;
  private final Javalin app;

  public QrCodeGeneratorApp(int port, @Nonnull Set<String> exemptIps) {
    this.rateLimiter = new IpRateLimiter(15, Duration.ofMinutes(1), exemptIps);
    this.codeGenerator = new CodeGenerator(0xFFFFFF, 0x000000);
    this.app =
        Javalin.create()
            .get(
                "/api/create",
                context -> {
                  String ip = context.ip();
                  if (!this.rateLimiter.tryConsume(ip)) {
                    context.status(429).result("Rate limit exceeded.");
                    return;
                  }

                  ParameterQuery query = new ParameterQuery(context);

                  String dataParam = query.get("data");
                  if (dataParam == null || dataParam.isEmpty()) {
                    context.status(400).result("Data parameter is required.");
                    return;
                  }

                  QrCode.Ecc eccParam = query.getOrDefault("ecc", QrCode.Ecc.MEDIUM);

                  fetchAndDisplayQrCode(context, dataParam, eccParam);
                })
            .start(port);
    Runtime.getRuntime().addShutdownHook(new Thread(this.app::stop));
  }

  private void fetchAndDisplayQrCode(
      @Nonnull Context context, @Nonnull String data, @Nonnull QrCode.Ecc ecc) {
    try {
      int hashCode = data.hashCode() + ecc.hashCode();

      byte[] imageData = this.cache.get(hashCode);
      if (imageData == null) {
        BufferedImage image = this.codeGenerator.generate(data, ecc, 10, 3);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        imageData = outputStream.toByteArray();

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
  public ExpiringMap<Integer, byte[]> getCache() {
    return this.cache;
  }

  @Nonnull
  public IpRateLimiter getRateLimiter() {
    return this.rateLimiter;
  }

  @Nonnull
  public CodeGenerator getCodeGenerator() {
    return this.codeGenerator;
  }

  @Nonnull
  public Javalin getApp() {
    return this.app;
  }
}
