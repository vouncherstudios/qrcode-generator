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
    implementation("io.javalin:javalin:6.1.6")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("net.jodah:expiringmap:0.5.11")
    implementation("com.bucket4j:bucket4j_jdk11-core:8.13.1")
    implementation("info.picocli:picocli:4.7.6")
    annotationProcessor("info.picocli:picocli-codegen:4.7.6")
}

indra {
    javaVersions {
        target(11)
    }

    github("vouncherstudios", "qrcode-creator")
    mitLicense()

    configurePublications {
        pom {
            organization {
                name.set("Vouncher Studios")
                url.set("https://github.com/vouncherstudios")
            }
            developers {
                developer {
                    id.set("WitchBoo")
                    name.set("Luís Mendes")
                    email.set("soconfirmo@hotmail.com")
                    timezone.set("America/Sao_Paulo")
                }
            }
        }
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