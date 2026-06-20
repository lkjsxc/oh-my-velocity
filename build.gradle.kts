plugins {
    java
}

group = "com.ohmyvelocity"
version = "0.1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.kyori:adventure-text-minimessage:4.17.0")
}

tasks.test {
    useJUnitPlatform()
}
