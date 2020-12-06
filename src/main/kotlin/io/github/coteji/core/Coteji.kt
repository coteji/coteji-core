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
import io.github.coteji.model.CotejiTest

class Coteji {
    lateinit var testsSource: TestsSource
    lateinit var testsTarget: TestsTarget

    /**
     * Pushes all the tests found in the Source, to the Target.
     * Deletes all the tests in the Target that are not present in the Source (match by id).
     * If force is false, tests that are already in the Target (by id) are not updated.
     */
    fun syncAll(force: Boolean = false, idUpdateMode: IdUpdateMode = IdUpdateMode.UPDATE) {
        val tests = testsSource.getAll()
        printSourceTestsStats(tests)
        val testsWithoutId = tests.filter { it.id == null }
        if (testsWithoutId.isNotEmpty()) {
            if (idUpdateMode == IdUpdateMode.ERROR) {
                printTestsWithoutId("ERROR", testsWithoutId)
                throw TestSourceException("There were tests without ID. See the list above.")
            }
            if (idUpdateMode == IdUpdateMode.WARNING) {
                printTestsWithoutId("WARNING", testsWithoutId)
            }
        }
        val result = testsTarget.pushAll(tests, force)
        testsSource.updateIdentifiers(result.testsAdded)
        result.print()
    }

    /**
     * Pushes selected by searchCriteria tests in the Source, to the Target.
     * If force is false, tests that are already in the Target (by id) are not updated.
     */
    fun syncOnly(searchCriteria: String, force: Boolean = false, idUpdateMode: IdUpdateMode = IdUpdateMode.UPDATE) {
        val tests = testsSource.getTests(searchCriteria)
        printSourceTestsStats(tests)
        val testsWithoutId = tests.filter { it.id == null }
        if (testsWithoutId.isNotEmpty()) {
            if (idUpdateMode == IdUpdateMode.ERROR) {
                printTestsWithoutId("ERROR", testsWithoutId)
                throw TestSourceException("There were tests without ID. See the list above.")
            }
            if (idUpdateMode == IdUpdateMode.WARNING) {
                printTestsWithoutId("WARNING", testsWithoutId)
            }
        }
        val result = testsTarget.pushOnly(tests, force)
        testsSource.updateIdentifiers(result.testsAdded)
        result.print()
    }

    /**
     * Finds all tests in the Source without IDs and pushes them to the Target.
     */
    fun pushNew() {
        val tests = testsSource.getAll().filter { it.id == null }
        printSourceTestsStats(tests)
        val result = testsTarget.pushOnly(tests, true)
        testsSource.updateIdentifiers(result.testsAdded)
        result.print()
    }

    /**
     * Emulates the result of syncAll action without actually doing anything, just logs the results to the console.
     */
    fun dryRun(force: Boolean = false) {
        val tests = testsSource.getAll()
        printSourceTestsStats(tests)
        val result = testsTarget.dryRun(tests, force)
        result.print()
    }


    /**
     * Prints out the tests found by searchCriteria.
     */
    fun trySearchCriteria(searchCriteria: String) {
        val tests = testsSource.getTests(searchCriteria)
        println("Found tests:")
        tests.forEach { println(it) }
        println("Total: ${tests.size}")
    }

    private fun printSourceTestsStats(tests: List<CotejiTest>) {
        val testsWithIdCount = tests.filter { it.id != null }.size
        println("Tests found: ${tests.size}; with ID: ${testsWithIdCount}; without ID: ${tests.size - testsWithIdCount}")
    }

    private fun printTestsWithoutId(level: String, tests: List<CotejiTest>) {
        println("$level: This tests' IDs are missing:")
        tests.forEach { println(it) }
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