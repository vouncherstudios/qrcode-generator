plugins {
    id("com.vouncherstudios.strawberry") version "1.0.1"
    id("net.kyori.blossom") version "2.1.0"
    id("net.kyori.indra.licenser.spotless") version "3.1.3"
}

group = "com.vouncherstudios"
version = "1.1.0"
description = "A QR code generator to use in our services."

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    implementation("io.javalin:javalin:6.3.0")
    implementation("ch.qos.logback:logback-classic:1.5.8")
    implementation("net.jodah:expiringmap:0.5.11")
    implementation("com.bucket4j:bucket4j_jdk11-core:8.14.0")
    implementation("info.picocli:picocli:4.7.6")
    annotationProcessor("info.picocli:picocli-codegen:4.7.6")
    implementation("io.nayuki:qrcodegen:1.8.0")
}

indra {
    javaVersions {
        target(11)
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "com.vouncherstudios.qrcodegenerator.QrCodeGeneratorBootstrap"
        }
    }
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
                property("description", project.description)
            }
        }
    }
}

indraSpotlessLicenser {
    licenseHeaderFile(file("LICENSE"))
    newLine(true)
}