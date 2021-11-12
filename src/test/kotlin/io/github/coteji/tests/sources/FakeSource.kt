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
package io.github.coteji.tests.sources

import io.github.coteji.core.Coteji
import io.github.coteji.core.TestsSource
import io.github.coteji.model.CotejiTest

class FakeSource : TestsSource {
    companion object {
        val localTests = mutableListOf<CotejiTest>()
    }

    override fun getTests(query: String): List<CotejiTest> {
        return localTests.filter { query in it.name }
    }

    override fun getAll(): List<CotejiTest> {
        return localTests
    }

    override fun updateIdentifiers(tests: List<CotejiTest>) {
        localTests.replaceAll { currentTest ->
            tests.find { it.name == currentTest.name && it.content == currentTest.content } ?: currentTest
        }
    }

}

infix fun Coteji.fakeSource(init: FakeSource.() -> Unit): FakeSource {
    val fakeSource = FakeSource()
    fakeSource.init()
    return fakeSource
}
