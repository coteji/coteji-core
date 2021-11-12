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
package io.github.coteji.tests.targets

import io.github.coteji.core.Coteji
import io.github.coteji.core.TestsTarget
import io.github.coteji.model.CotejiTest
import io.github.coteji.model.PushResult
import io.github.coteji.tests.sources.FakeSource
import kotlin.random.Random

class FakeTarget : TestsTarget {
    companion object {
        val remoteTests = mutableListOf<CotejiTest>()
    }

    override fun pushAll(tests: List<CotejiTest>, force: Boolean): PushResult {
        val result = PushResult()

        val idsInSource = tests.filter { it.id != null }.map { it.id }
        result.testsDeleted.addAll(remoteTests.filter { it.id !in idsInSource }.map { it.id!! })
        remoteTests.removeIf { it.id !in idsInSource }

        val idsInTarget = remoteTests.map { it.id }
        var nextId = 100
        tests.forEach {
            if (it.id == null || it.id !in idsInTarget) {
                val newTest = CotejiTest(
                        id = "$nextId",
                        name = it.name,
                        content = it.content,
                        attributes = it.attributes)
                nextId++
                remoteTests.add(newTest)
                result.testsAdded.add(newTest)
            } else {
                if (force) {
                    val index = remoteTests.indexOfFirst { targetTest -> targetTest.id == it.id }
                    remoteTests.removeAt(index)
                    remoteTests.add(index, it)
                    result.testsUpdated.add(it)
                } else {
                    result.testsAlreadyUpToDate.add(it)
                }
            }
            if (it.id != null && it.id !in idsInTarget) {
                result.testsWithNonExistingId.add(it)
            }
        }
        return result
    }

    override fun pushOnly(tests: List<CotejiTest>, force: Boolean): PushResult {
        val result = PushResult()
        val idsInTarget = remoteTests.map { it.id }
        var nextId = 100
        tests.forEach {
            if (it.id == null || it.id !in idsInTarget) {
                val newTest = CotejiTest(
                        id = "$nextId",
                        name = it.name,
                        content = it.content,
                        attributes = it.attributes)
                nextId++
                remoteTests.add(newTest)
                result.testsAdded.add(newTest)
            } else {
                if (force) {
                    val index = remoteTests.indexOfFirst { targetTest -> targetTest.id == it.id }
                    remoteTests.removeAt(index)
                    remoteTests.add(index, it)
                    result.testsUpdated.add(it)
                } else {
                    result.testsAlreadyUpToDate.add(it)
                }
            }
            if (it.id != null && it.id !in idsInTarget) {
                result.testsWithNonExistingId.add(it)
            }
        }
        return result
    }

    override fun dryRun(tests: List<CotejiTest>, force: Boolean): PushResult {
        val result = PushResult()
        val idsInSource = tests.filter { it.id != null }.map { it.id }
        result.testsDeleted.addAll(remoteTests.filter { it.id !in idsInSource }.map { it.id!! })
        val idsInTarget = remoteTests.map { it.id }
        tests.forEach {
            if (it.id == null || it.id !in idsInTarget) {
                val newTest = CotejiTest(
                        id = "COT-${Random.nextInt(10000)}",
                        name = it.name,
                        content = it.content,
                        attributes = it.attributes)
                result.testsAdded.add(newTest)
            } else {
                if (force) {
                    result.testsUpdated.add(it)
                } else {
                    result.testsAlreadyUpToDate.add(it)
                }
            }
            if (it.id != null && it.id !in idsInTarget) {
                result.testsWithNonExistingId.add(it)
            }
        }
        return result
    }
}

infix fun Coteji.fakeTarget(init: FakeTarget.() -> Unit): FakeTarget {
    val fakeTarget = FakeTarget()
    fakeTarget.init()
    return fakeTarget
}
