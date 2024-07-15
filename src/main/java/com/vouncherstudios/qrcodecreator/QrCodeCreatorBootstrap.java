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

package com.vouncherstudios.qrcodecreator;

import com.vouncherstudios.qrcodecreator.app.QrCodeCreatorApp;
import javax.annotation.Nonnull;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "qr-code-creator",
    mixinStandardHelpOptions = true,
    version = BuildParameters.VERSION,
    description = BuildParameters.DESCRIPTION)
public final class QrCodeCreatorBootstrap implements Runnable {
  public QrCodeCreatorBootstrap() {}

  private static QrCodeCreatorApp app;

  @Option(
      names = {"-p", "--port"},
      description = "The port to run the server on")
  private int port = 7000;

  public static void main(String[] args) {
    new CommandLine(new QrCodeCreatorBootstrap()).execute(args);
  }

  @Override
  public void run() {
    app = new QrCodeCreatorApp(this.port);
  }

  @Nonnull
  public static QrCodeCreatorApp getApp() {
    return app;
  }
}