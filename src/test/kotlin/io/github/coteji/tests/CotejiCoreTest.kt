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

package io.github.coteji.tests

import io.github.coteji.config.ConfigCotejiScript
import io.github.coteji.core.Coteji
import io.github.coteji.model.CotejiTest
import io.github.coteji.tests.sources.FakeSource
import io.github.coteji.tests.targets.FakeTarget
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import javax.script.ScriptException
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.implicitReceivers
import kotlin.script.experimental.api.onFailure
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

class CotejiCoreTest {

    private lateinit var coteji: Coteji
    private val createUserTest: CotejiTest = CotejiTest(name = "createUser", content = "some content",
            attributes = mapOf(Pair("component", "users"), Pair("type", "api")))
    private val updateUserTest: CotejiTest = CotejiTest(name = "updateUser", content = "some content updated",
            attributes = mapOf(Pair("component", "users"), Pair("type", "ui")))
    private val createUserTestWithId: CotejiTest = CotejiTest(id = "1", name = "createUser", content = "some content",
            attributes = mapOf(Pair("component", "users"), Pair("type", "api")))
    private val updateUserTestWithId: CotejiTest = CotejiTest(id = "2", name = "updateUser", content = "some content updated",
            attributes = mapOf(Pair("component", "users"), Pair("type", "ui")))

    @BeforeEach
    fun setUp() {
        val source = File("src/test/resources/config.coteji.kts").toScriptSource()
        val configuration = createJvmCompilationConfigurationFromTemplate<ConfigCotejiScript>()
        coteji = Coteji()

        BasicJvmScriptingHost().eval(source, configuration, ScriptEvaluationConfiguration {
            implicitReceivers(coteji)
        }).onFailure { result ->
            result.reports.subList(0, result.reports.size - 1).forEach { println(it) }
            val error = result.reports.last()
            val location = error.location?.start
            throw ScriptException("${error.message} (${error.sourcePath}:${location?.line}:${location?.col})")
        }
    }

    @Test
    fun syncTestNew() {
        // arrange
        FakeSource.localTests.clear()
        FakeSource.localTests.addAll(listOf(createUserTest, updateUserTest))
        FakeTarget.remoteTests.clear()
        // act
        coteji.syncOnly(createUserTest.name, true)
        // assert
        assert(FakeTarget.remoteTests.size == 1)
        val newTest = FakeTarget.remoteTests[0]
        assert(newTest.id != null)
        assert(newTest.name == createUserTest.name)
        assert(newTest.content == createUserTest.content)
        assert(newTest.attributes == createUserTest.attributes)
    }

    @Test
    fun syncTestExisting() {
        // arrange
        FakeSource.localTests.clear()
        val createUserTestChanged = createUserTestWithId.copy(content = "new content")
        FakeSource.localTests.addAll(listOf(createUserTestChanged, updateUserTestWithId))
        FakeTarget.remoteTests.clear()
        FakeTarget.remoteTests.addAll(listOf(createUserTestWithId, updateUserTestWithId))
        // act
        coteji.syncOnly(createUserTest.name, true)
        // assert
        assert(FakeTarget.remoteTests.size == 2)
        assert(FakeTarget.remoteTests.contains(createUserTestChanged))
    }

}