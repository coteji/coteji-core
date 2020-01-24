/*
 * Copyright (c) 2019 Coteji AUTHORS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
plugins {
    application
    kotlin("jvm") version "1.3.61"
}

version = "1.0.0"
group = "io.github.coteji"

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-scripting-common:1.3.61")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:1.3.61")
    implementation("com.github.javaparser:javaparser-core:3.14.7")
}