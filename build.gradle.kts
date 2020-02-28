plugins {
    kotlin("jvm") version "1.3.61"
}

version = "1.0.0"
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
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}