plugins {
    kotlin("jvm") version "1.5.31"
    id("com.vanniktech.maven.publish") version "0.13.0"
    id("org.jetbrains.dokka") version "1.4.32"
    jacoco
}

version = "0.1.2"
group = "io.github.coteji"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("scripting-jvm"))
    implementation(kotlin("scripting-jvm-host"))
    implementation(kotlin("scripting-common"))
    implementation(kotlin("scripting-dependencies"))
    implementation(kotlin("scripting-dependencies-maven"))
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.6")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.assertj:assertj-core:3.21.0")
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.97".toBigDecimal()
            }
        }
    }
}

tasks.test {
    dependsOn("cleanTest")
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    finalizedBy(tasks.jacocoTestReport)
}