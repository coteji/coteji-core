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
import io.github.coteji.model.Test as cotejiTest
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

class CotejiTest {

    private lateinit var coteji: Coteji

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
    fun syncTest() {
        // arrange
        FakeSource.localTests.clear()
        FakeSource.localTests.add(
                cotejiTest(name = "createUser", content = "some content",
                        attributes = mapOf(Pair("component", "users"), Pair("type", "api"))))
        FakeSource.localTests.add(
                cotejiTest(name = "updateUser", content = "some content updated",
                        attributes = mapOf(Pair("component", "users"), Pair("type", "ui"))))
        // act
        coteji.syncTest("createUser")
        // assert
        assert(FakeTarget.remoteTests.size == 1)
        assert(FakeTarget.remoteTests[0] == cotejiTest(
                name = "createUser", content = "some content",
                attributes = mapOf(Pair("component", "users"), Pair("type", "api"))))
    }

}