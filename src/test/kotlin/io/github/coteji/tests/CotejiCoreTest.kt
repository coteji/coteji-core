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

package io.github.coteji.tests

import io.github.coteji.config.ConfigCotejiScript
import io.github.coteji.core.Coteji
import io.github.coteji.model.CotejiTest
import io.github.coteji.tests.sources.FakeSource
import io.github.coteji.tests.targets.FakeTarget
import org.assertj.core.api.Assertions.assertThat
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
    private val sourceTestWithoutId: CotejiTest = CotejiTest(
        name = "createUser",
        content = "some content",
        attributes = mapOf(Pair("component", "users"), Pair("type", "api"))
    )
    private val sourceTestWithIdZero: CotejiTest = CotejiTest(
        id = "0",
        name = "updateUser",
        content = "some content updated",
        attributes = mapOf(Pair("component", "users"), Pair("type", "ui"))
    )
    private val sourceTestWithIdOne: CotejiTest = CotejiTest(
        id = "1",
        name = "createConfig",
        content = "some config",
        attributes = mapOf(Pair("component", "config"), Pair("type", "ui"))
    )
    private val targetTestWithIdOne: CotejiTest = CotejiTest(
        id = "1",
        name = "createConfig",
        content = "some config old",
        attributes = mapOf(Pair("component", "config"))
    )
    private val targetTestWithIdTwo: CotejiTest = CotejiTest(
        id = "2",
        name = "updateConfig",
        content = "some update config",
        attributes = mapOf(Pair("component", "config"), Pair("type", "unit"))
    )

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
        FakeSource.localTests.clear()
        FakeSource.localTests.addAll(listOf(sourceTestWithoutId, sourceTestWithIdZero, sourceTestWithIdOne))
        FakeTarget.remoteTests.clear()
        FakeTarget.remoteTests.addAll(listOf(targetTestWithIdOne, targetTestWithIdTwo))
    }

    @Test
    fun syncAllForce() {
        coteji.syncAll(true)
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            sourceTestWithIdOne
        )
    }

    @Test
    fun syncAllNotForce() {
        coteji.syncAll()
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            targetTestWithIdOne
        )
    }
    @Test
    fun syncOnlyForce() {
        coteji.syncOnly("a", true)
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            sourceTestWithIdOne,
            targetTestWithIdTwo
        )
    }

    @Test
    fun syncOnlyNotForce() {
        coteji.syncOnly("a")
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            targetTestWithIdOne,
            targetTestWithIdTwo
        )
    }

    @Test
    fun pushNew() {
        coteji.pushNew()
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            targetTestWithIdOne,
            targetTestWithIdTwo
        )
    }

    @Test
    fun dryRun() {
        coteji.dryRun()
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            targetTestWithIdOne,
            targetTestWithIdTwo
        )
    }
}