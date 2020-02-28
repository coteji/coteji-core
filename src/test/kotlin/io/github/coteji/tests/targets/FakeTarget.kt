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

    override fun pushAll(tests: List<Test>): List<Test> {
        remoteTests.clear()
        val result = mutableListOf<Test>()
        tests.forEach {
            result.add(Test(
                    id = it.id ?: "COT-${Random.nextInt(10000)}",
                    name = it.name,
                    content = it.content,
                    attributes = it.attributes))
        }
        remoteTests.addAll(result)
        return result
    }
}