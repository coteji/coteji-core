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

class Coteji {
    lateinit var testsSource: TestsSource
    lateinit var testsTarget: TestsTarget

    fun syncAll(force: Boolean = false) {
        testsTarget.pushAll(testsSource.getAll(), force)
    }

    fun syncOnly(searchCriteria: String, force: Boolean = false) {
        testsTarget.pushOnly(testsSource.getTests(searchCriteria), force)
    }

    fun syncTest(searchCriteria: String) {
        val test = testsSource.getTest(searchCriteria)
                ?: throw TestSourceException("Test not found by criteria: '$searchCriteria'")
        testsTarget.push(test)
    }

    fun dryRun() {
        val sourceTests = testsSource.getAll()
        val targetTests = testsTarget.getAll()
        val sourceIds = sourceTests.map { it.id }
        val targetIds = targetTests.map { it.id }
        val matchedIds = mutableListOf<String>()
        sourceIds.forEach {
            if (it != null && it in targetIds) {
                matchedIds.add(it)
            }
        }
        println("""
            Total tests in Source: ${sourceTests.size}            
            Total tests in Target: ${targetTests.size}
            Tests matched (by ID): ${matchedIds.size}
        """.trimIndent())
    }
}

var Coteji.source: TestsSource
    get() = testsSource
    set(value) {
        testsSource = value
    }

var Coteji.target: TestsTarget
    get() = testsTarget
    set(value) {
        testsTarget = value
    }