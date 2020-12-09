plugins {
    kotlin("jvm") version "1.3.72"
    id("com.vanniktech.maven.publish") version "0.13.0"
    id("org.jetbrains.dokka") version "0.10.1"
}

version = "0.1.0"
group = "io.github.coteji"

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("scripting-jvm"))
    implementation(kotlin("scripting-jvm-host-embeddable"))
    implementation(kotlin("script-util"))
    implementation("com.github.javaparser:javaparser-core:3.14.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.assertj:assertj-core:3.18.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}