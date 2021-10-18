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
import io.github.coteji.core.source
import io.github.coteji.core.target
import io.github.coteji.model.CotejiTest
import io.github.coteji.tests.sources.FakeSource
import io.github.coteji.tests.targets.FakeTarget
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import javax.script.ScriptException
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.implicitReceivers
import kotlin.script.experimental.api.onFailure
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CotejiCoreTest {

    private val logger = KotlinLogging.logger {}
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
            result.reports.subList(0, result.reports.size - 1).forEach { logger.error { it } }
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
    fun `sync all force`() {
        val result = coteji.syncAll(force = true)
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            sourceTestWithIdOne
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(3)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(1)
        assertThat(result.pushResult.testsAdded).hasSize(2)
        assertThat(result.pushResult.testsUpdated).hasSize(1)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(0)
        assertThat(result.pushResult.testsDeleted).hasSize(1)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `sync all no force`() {
        val result = coteji.syncAll()
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            targetTestWithIdOne
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(3)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(1)
        assertThat(result.pushResult.testsAdded).hasSize(2)
        assertThat(result.pushResult.testsUpdated).hasSize(0)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(1)
        assertThat(result.pushResult.testsDeleted).hasSize(1)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `sync all no force no updating IDs`() {
        val result = coteji.syncAll(force = false, updateIdsInSource = false)
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            targetTestWithIdOne
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId,
            sourceTestWithIdZero,
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(3)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(1)
        assertThat(result.pushResult.testsAdded).hasSize(2)
        assertThat(result.pushResult.testsUpdated).hasSize(0)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(1)
        assertThat(result.pushResult.testsDeleted).hasSize(1)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `sync only force`() {
        val result = coteji.syncOnly("a", force = true)
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            sourceTestWithIdOne,
            targetTestWithIdTwo
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(3)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(1)
        assertThat(result.pushResult.testsAdded).hasSize(2)
        assertThat(result.pushResult.testsUpdated).hasSize(1)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(0)
        assertThat(result.pushResult.testsDeleted).hasSize(0)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `sync only no force`() {
        val result = coteji.syncOnly("a")
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            targetTestWithIdOne,
            targetTestWithIdTwo
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(3)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(1)
        assertThat(result.pushResult.testsAdded).hasSize(2)
        assertThat(result.pushResult.testsUpdated).hasSize(0)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(1)
        assertThat(result.pushResult.testsDeleted).hasSize(0)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `sync only no force no updating IDs`() {
        val result = coteji.syncOnly("a", force = false, updateIdsInSource = false)
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero.copy(id = "101"),
            targetTestWithIdOne,
            targetTestWithIdTwo
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId,
            sourceTestWithIdZero,
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(3)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(1)
        assertThat(result.pushResult.testsAdded).hasSize(2)
        assertThat(result.pushResult.testsUpdated).hasSize(0)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(1)
        assertThat(result.pushResult.testsDeleted).hasSize(0)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `push new`() {
        val result = coteji.pushNew()
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            targetTestWithIdOne,
            targetTestWithIdTwo
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId.copy(id = "100"),
            sourceTestWithIdZero,
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(1)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(0)
        assertThat(result.pushResult.testsAdded).hasSize(1)
        assertThat(result.pushResult.testsUpdated).hasSize(0)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(0)
        assertThat(result.pushResult.testsDeleted).hasSize(0)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `dry run force`() {
        val result = coteji.dryRun(force = true)
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            targetTestWithIdOne,
            targetTestWithIdTwo
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId,
            sourceTestWithIdZero,
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(3)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(1)
        assertThat(result.pushResult.testsAdded).hasSize(2)
        assertThat(result.pushResult.testsUpdated).hasSize(1)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(0)
        assertThat(result.pushResult.testsDeleted).hasSize(1)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `dry run no force`() {
        val result = coteji.dryRun()
        assertThat(FakeTarget.remoteTests).containsExactlyInAnyOrder(
            targetTestWithIdOne,
            targetTestWithIdTwo
        )
        assertThat(FakeSource.localTests).containsExactlyInAnyOrder(
            sourceTestWithoutId,
            sourceTestWithIdZero,
            sourceTestWithIdOne
        )
        assertThat(result.testsFoundInSourceCount).isEqualTo(3)
        assertThat(result.testsWithoutId).hasSize(1)
        assertThat(result.pushResult.testsWithNonExistingId).hasSize(1)
        assertThat(result.pushResult.testsAdded).hasSize(2)
        assertThat(result.pushResult.testsUpdated).hasSize(0)
        assertThat(result.pushResult.testsAlreadyUpToDate).hasSize(1)
        assertThat(result.pushResult.testsDeleted).hasSize(1)
        assertThat(result.pushResult.testsSyncFailed).hasSize(0)
    }

    @Test
    fun `try query`() {
        val tests = coteji.tryQuery("create")
        assertThat(tests)
            .containsExactlyInAnyOrder(
                sourceTestWithoutId,
                sourceTestWithIdOne
            )
    }

    @Test
    fun `coteji property getters`() {
        assertThat(coteji.source).isNotNull
        assertThat(coteji.target).isNotNull
    }
}