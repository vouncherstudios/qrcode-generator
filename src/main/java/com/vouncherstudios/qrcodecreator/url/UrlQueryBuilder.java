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

package com.vouncherstudios.qrcodecreator.url;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public final class UrlQueryBuilder {
  private final String url;
  private final Map<String, String> parameters = new LinkedHashMap<>();

  public UrlQueryBuilder(@Nonnull String url) {
    this.url = url;
  }

  public UrlQueryBuilder add(@Nonnull String key, @Nonnull String value) {
    this.parameters.put(key, value);
    return this;
  }

  public String toURL() {
    if (this.parameters.isEmpty()) {
      return this.url;
    }

    StringBuilder builder = new StringBuilder(this.url);
    int parameterIndex = 0;

    for (String key : this.parameters.keySet()) {
      if (parameterIndex == 0) {
        builder.append("?");
      } else {
        builder.append("&");
      }

      String encodedValue = URLEncoder.encode(this.parameters.get(key), StandardCharsets.UTF_8);

      builder.append(key).append("=").append(encodedValue);
      parameterIndex++;
    }

    return builder.toString();
  }
}
