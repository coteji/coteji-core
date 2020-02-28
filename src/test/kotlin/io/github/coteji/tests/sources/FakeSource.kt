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
package io.github.coteji.tests.sources

import io.github.coteji.core.TestsSource
import io.github.coteji.model.Test

class FakeSource(private val component: String) : TestsSource {

    override fun getTest(searchCriteria: String): Test {
        return Test(name = "createUser", content = searchCriteria,
                attributes = mapOf(Pair("component", "users"), Pair("type", "api")))
    }

    override fun getTests(searchCriteria: String): List<Test> {
        return listOf(
                Test(name = "createUser", content = searchCriteria,
                        attributes = mapOf(Pair("component", "users"), Pair("type", "api"))),
                Test(name = "deleteUser", content = searchCriteria,
                        attributes = mapOf(Pair("component", "users"), Pair("type", "ui")))
        )
    }

    override fun getAllTests(): List<Test> {
        return listOf(
                Test(name = "createUser", content = "some content",
                        attributes = mapOf(Pair("component", "users"), Pair("type", "api"))),
                Test(name = "updateUser", content = "some content updated",
                        attributes = mapOf(Pair("component", "users"), Pair("type", "ui"))),
                Test(name = "deleteUser", content = "some content deleted",
                        attributes = mapOf(Pair("component", "users"), Pair("type", "ui")))
        )
    }

}