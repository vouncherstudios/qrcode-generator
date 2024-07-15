# QR Code Creator
A QR code creator to use in our services.

## Introduction
QR Code Creator is a versatile QR code generation tool designed to integrate seamlessly with various services. Built with Java, it offers customizable QR code generation capabilities, including adjustable colors, error correction levels, and rate limiting for IP addresses.

## Features
- Generate QR codes with customizable foreground and background colors.
- Support for different error correction levels to enhance readability.
- IP rate limiting to prevent abuse of the service.

## Requirements
- Java 11 or higher
- Gradle (for building the project)

## Installation
Clone the repository and build the project using Gradle:

```sh
git clone https://github.com/vouncherstudios/qrcode-creator.git
cd qrcode-creator
./gradlew build
```

## Usage
To start the qrcode-creator service, run:

```sh
java -jar build/libs/qrcode-creator-1.0.0.jar
```

You can customize the port and exempt IP addresses from rate limiting using command-line options.

## Configuration
- **Port**: The service port can be set using the `-p` or `--port` option.
- **Exempt IPs**: To exempt certain IP addresses from rate limiting, use the `-e` or `--exempt` option followed by the IP addresses.

## Official Usage Route

The official route for using the QR Code Creator service is hosted at `https://qrcode.vouncherstudios.com`. You can generate QR codes by accessing the `/api/create` endpoint with the required parameters.

For example, to generate a QR code with default settings, you can use the following URL:

```
https://qrcode.vouncherstudios.com/api/create?data=YourDataHere
```

Replace `YourDataHere` with the data you want to encode in the QR code.

## License

QR Code Creator is released under the MIT License. See the [LICENSE](LICENSE) file for more details.