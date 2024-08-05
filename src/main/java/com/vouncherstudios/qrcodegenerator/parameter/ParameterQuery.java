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

package com.vouncherstudios.qrcodegenerator.parameter;

import io.javalin.http.Context;
import io.nayuki.qrcodegen.QrCode;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ParameterQuery {
  private final Context context;

  public ParameterQuery(@Nonnull Context context) {
    this.context = context;
  }

  @Nullable
  public String get(@Nonnull String key) {
    return this.context.queryParam(key);
  }

  @Nonnull
  public QrCode.Ecc getOrDefault(@Nonnull String key, @Nonnull QrCode.Ecc defaultValue) {
    String parameterValue = this.context.queryParam(key);
    if (parameterValue == null) {
      return defaultValue;
    }

    for (QrCode.Ecc value : QrCode.Ecc.values()) {
      if (value.name().equalsIgnoreCase(parameterValue)) {
        return value;
      }
    }

    switch (parameterValue) {
      case "L":
        return QrCode.Ecc.LOW;
      case "Q":
        return QrCode.Ecc.QUARTILE;
      case "H":
        return QrCode.Ecc.HIGH;
      default:
        return defaultValue;
    }
  }
}
