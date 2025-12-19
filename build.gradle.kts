plugins {
    id("java-library")
    id("maven-publish")
}

group = "com.p6spy"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.slf4j:slf4j-api:1.7.25")
    compileOnly("org.slf4j:slf4j-log4j12:1.7.25")
    compileOnly("log4j:log4j:1.2.17")

    compileOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.17.1")
    compileOnly("org.apache.logging.log4j:log4j-api:2.17.1")
    compileOnly("org.apache.logging.log4j:log4j-core:2.17.1")

    compileOnly("ch.qos.logback:logback-classic:1.2.3")
    compileOnly("ch.qos.logback:logback-core:1.2.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
