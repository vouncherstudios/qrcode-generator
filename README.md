# QR Code Generator
A QR code generator to use in our services.

## Introduction
QR Code Generator is a versatile QR code generation tool designed to integrate seamlessly with various services. Built with Java, it offers customizable QR code generation capabilities, including adjustable colors, error correction levels, and rate limiting for IP addresses.

## Features
- Generate QR codes with customizable foreground and background colors.
- Support for different error correction levels to enhance readability.
- IP rate limiting to prevent abuse of the service.

## Requirements
- Java 11 or higher

## Installation
Clone the repository and build the project using Gradle:

```sh
git clone https://github.com/vouncherstudios/qrcode-generator.git
cd qrcode-generator
./gradlew build
```

## Usage
To start the qrcode-creator service, replace {VERSION} with the current version, run:

```sh
java -jar build/libs/qrcode-generator-{VERSION}.jar
```

You can customize the port and exempt IP addresses from rate limiting using command-line options.

## Configuration
- **Port**: The service port can be set using the `-p` or `--port` option.
- **Exempt IPs**: To exempt certain IP addresses from rate limiting, use the `-e` or `--exempt` option followed by the IP addresses.

## Official Usage Route

The official route for using the QR Code Generator service is hosted at `https://qrcode.vouncherstudios.com`. You can generate QR codes by accessing the `/api/create` endpoint with the required parameters.

For example, to generate a QR code with default settings, you can use the following URL:

```
https://qrcode.vouncherstudios.com/api/create?data=YourDataHere
```

Replace `YourDataHere` with the data you want to encode in the QR code.

## License

QR Code Generator is released under the MIT License. See the [LICENSE](LICENSE) file for more details.