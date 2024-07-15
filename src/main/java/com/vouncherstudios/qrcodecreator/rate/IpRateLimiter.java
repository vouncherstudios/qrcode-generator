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

package com.vouncherstudios.qrcodecreator.rate;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

public final class IpRateLimiter {
  private final int limit;
  private final Duration period;
  private final ExpiringMap<String, Bucket> ipBuckets;

  public IpRateLimiter(int limit, @Nonnull Duration period) {
    this.limit = limit;
    this.period = period;
    this.ipBuckets =
        ExpiringMap.builder()
            .maxSize(1000)
            .expiration(period.toMillis(), TimeUnit.MILLISECONDS)
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .build();
  }

  public boolean tryConsume(@Nonnull String ipAddress) {
    Bucket bucket = this.ipBuckets.computeIfAbsent(ipAddress, this::createNewBucket);
    return bucket.tryConsume(1);
  }

  private Bucket createNewBucket(@Nonnull String ipAddress) {
    return Bucket.builder()
        .addLimit(
            Bandwidth.builder()
                .capacity(this.limit)
                .refillIntervally(this.limit, Duration.ofMillis(this.period.toMillis()))
                .build())
        .build();
  }
}
