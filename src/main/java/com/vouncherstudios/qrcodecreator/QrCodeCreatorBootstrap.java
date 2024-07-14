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
