/*
 *    Copyright (c) 2020 - 2021 Coteji AUTHORS.
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

import io.github.coteji.model.CotejiTest
import io.github.coteji.model.SyncResult

class Coteji {
    lateinit var testsSource: TestsSource
    lateinit var testsTarget: TestsTarget

    /**
     * Pushes all the tests found in the Source, to the Target.
     * Deletes all the tests in the Target that are not present in the Source (match by id).
     * If force is false, tests that are already in the Target (by id) are not updated.
     */
    fun syncAll(force: Boolean = false, updateIdsInSource: Boolean = true): SyncResult {
        val tests = testsSource.getAll()
        val pushResult = testsTarget.pushAll(tests, force)
        val syncResult = SyncResult()
        syncResult.testsFoundInSourceCount = tests.size
        syncResult.testsWithoutId = tests.filter { it.id == null }
        syncResult.pushResult = pushResult
        if (updateIdsInSource) {
            testsSource.updateIdentifiers(pushResult.testsAdded)
        }
        return syncResult
    }

    /**
     * Pushes selected by query tests in the Source, to the Target.
     * If force is false, tests that are already in the Target (by id) are not updated.
     */
    fun syncOnly(query: String, force: Boolean = false, updateIdsInSource: Boolean = true): SyncResult {
        val tests = testsSource.getTests(query)
        val pushResult = testsTarget.pushOnly(tests, force)
        val syncResult = SyncResult()
        syncResult.testsFoundInSourceCount = tests.size
        syncResult.testsWithoutId = tests.filter { it.id == null }
        syncResult.pushResult = pushResult
        if (updateIdsInSource) {
            testsSource.updateIdentifiers(pushResult.testsAdded)
        }
        return syncResult
    }

    /**
     * Finds all tests in the Source without IDs and pushes them to the Target.
     */
    fun pushNew(): SyncResult {
        val tests = testsSource.getAll().filter { it.id == null }
        val pushResult = testsTarget.pushOnly(tests, true)
        testsSource.updateIdentifiers(pushResult.testsAdded)
        val syncResult = SyncResult()
        syncResult.testsFoundInSourceCount = tests.size
        syncResult.testsWithoutId = tests
        syncResult.pushResult = pushResult
        return syncResult
    }

    /**
     * Emulates the result of syncAll action without actually doing anything, just logs the results to the console.
     */
    fun dryRun(force: Boolean = false): SyncResult {
        val tests = testsSource.getAll()
        val pushResult = testsTarget.dryRun(tests, force)
        val syncResult = SyncResult()
        syncResult.testsFoundInSourceCount = tests.size
        syncResult.testsWithoutId = tests.filter { it.id == null }
        syncResult.pushResult = pushResult
        return syncResult
    }

    /**
     * Get the list of tests from the Source found by query.
     */
    fun tryQuery(query: String): List<CotejiTest> = testsSource.getTests(query)

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