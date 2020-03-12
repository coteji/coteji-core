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
package io.github.coteji.tests.targets

import io.github.coteji.core.TestsTarget
import io.github.coteji.model.Test
import kotlin.random.Random

class FakeTarget : TestsTarget {
    companion object {
        val remoteTests = mutableListOf<Test>()
    }

    override fun push(test: Test): Test {
        if (test.id != null) {
            remoteTests.removeIf { it.id == test.id }
        }
        val remoteTest = Test(
                id = test.id ?: "COT-${Random.nextInt(10000)}",
                name = test.name,
                content = test.content,
                attributes = test.attributes)
        remoteTests.add(test)
        return remoteTest
    }

    override fun pushAll(tests: List<Test>, force: Boolean): List<Test> {
        if (force) {
            remoteTests.clear()
            tests.forEach {
                remoteTests.add(Test(
                        id = it.id ?: "COT-${Random.nextInt(10000)}",
                        name = it.name,
                        content = it.content,
                        attributes = it.attributes))
            }
        } else {
            val idsInSource = tests.filter { it.id != null }.map { it.id }
            if (idsInSource.isNotEmpty()) {
                remoteTests.removeIf { it.id !in idsInSource }
            }
            val idsInTarget = remoteTests.map { it.id }
            tests.forEach {
                if (it.id == null || it.id !in idsInTarget) {
                    remoteTests.add(Test(
                            id = it.id ?: "COT-${Random.nextInt(10000)}",
                            name = it.name,
                            content = it.content,
                            attributes = it.attributes))
                }
            }
        }
        return remoteTests
    }


    override fun pushOnly(tests: List<Test>, force: Boolean): List<Test> {
        val result = mutableListOf<Test>()
        if (force) {
            val idsInSource = tests.filter { it.id != null }.map { it.id }
            tests.forEach {
                result.add(Test(
                        id = it.id ?: "COT-${Random.nextInt(10000)}",
                        name = it.name,
                        content = it.content,
                        attributes = it.attributes))
            }
            if (idsInSource.isNotEmpty()) {
                remoteTests.removeIf { it.id in idsInSource }
            }
            remoteTests.addAll(result)
        } else {
            val idsInTarget = remoteTests.map { it.id }
            tests.forEach {
                if (it.id == null || it.id !in idsInTarget) {
                    val test = Test(
                            id = it.id ?: "COT-${Random.nextInt(10000)}",
                            name = it.name,
                            content = it.content,
                            attributes = it.attributes)
                    result.add(test)
                    remoteTests.add(test)
                } else {
                    result.add(remoteTests.first { t -> t.id == it.id})
                }
            }
        }
        return result
    }

    override fun getAll(): List<Test> = remoteTests
}