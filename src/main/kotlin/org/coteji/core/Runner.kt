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
package org.coteji.core

import org.coteji.config.Script
import org.coteji.sources.JavaCodeSource
import org.coteji.targets.JiraTarget

fun main(args: Array<String>) {
    Script().scriptBody()
}

class Runner {
    private lateinit var testsSource: TestsSource
    private lateinit var testsTarget: TestsTarget

    fun run() {
        testsSource = JavaCodeSource()
        testsTarget = JiraTarget()
        testsTarget.pushAll(testsSource.getAllTests())
    }

}