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

package com.vouncherstudios.qrcodecreator.code;

import io.nayuki.qrcodegen.QrCode;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

public final class CodeGenerator {
  private final int lightColor;
  private final int darkColor;

  public CodeGenerator(int lightColor, int darkColor) {
    this.lightColor = lightColor;
    this.darkColor = darkColor;
  }

  @Nonnull
  public BufferedImage generate(
      @Nonnull String text, @Nonnull QrCode.Ecc ecc, int scale, int margin) {
    QrCode code = QrCode.encodeText(text, ecc);
    BufferedImage result =
        new BufferedImage(
            (code.size + margin * 2) * scale,
            (code.size + margin * 2) * scale,
            BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < result.getHeight(); y++) {
      for (int x = 0; x < result.getWidth(); x++) {
        boolean color = code.getModule(x / scale - margin, y / scale - margin);
        result.setRGB(x, y, color ? darkColor : lightColor);
      }
    }
    return result;
  }
}
