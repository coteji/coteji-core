plugins {
    kotlin("jvm") version "1.5.31"
    id("com.vanniktech.maven.publish") version "0.13.0"
    id("org.jetbrains.dokka") version "1.4.32"
    jacoco
}

version = "0.2.0"
group = "io.github.coteji"

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("scripting-jvm"))
    api(kotlin("scripting-jvm-host"))
    api(kotlin("scripting-common"))
    api(kotlin("scripting-dependencies"))
    api(kotlin("scripting-dependencies-maven"))
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
                minimum = "0.92".toBigDecimal()
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