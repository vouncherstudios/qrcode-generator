import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.vouncherstudios.strawberry") version "1.0.0"
    id("net.kyori.blossom") version "2.1.0"
    id("net.kyori.indra.licenser.spotless") version "3.1.3"
}

group = "com.vouncherstudios"
version = "1.0.0"
description = "A QR code creator to use in our services."

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    implementation("io.javalin:javalin:6.2.0")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("net.jodah:expiringmap:0.5.11")
    implementation("com.bucket4j:bucket4j_jdk11-core:8.13.1")
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
    withType<ShadowJar> {
        manifest {
            attributes["Main-Class"] = "com.vouncherstudios.qrcodecreator.QrCodeCreatorBootstrap"
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