/*
 *    Copyright (c) 2020 Coteji AUTHORS.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.coteji.core

import io.github.coteji.exceptions.TestSourceException

class CotejiBuilder {
    lateinit var testsSource: TestsSource
    lateinit var testsTarget: TestsTarget

    fun syncAll() {
        testsTarget.pushAll(testsSource.getAllTests())
    }

    fun syncAll(searchCriteria: String) {
        testsTarget.pushAll(testsSource.getTests(searchCriteria))
    }

    fun syncTest(searchCriteria: String) {
        val test = testsSource.getTest(searchCriteria)
                ?: throw TestSourceException("Test not found by criteria: '$searchCriteria'")
        testsTarget.push(test)
    }

}

fun CotejiBuilder.setSource(source: TestsSource) {
    testsSource = source
}

fun CotejiBuilder.setTarget(target: TestsTarget) {
    testsTarget = target
}