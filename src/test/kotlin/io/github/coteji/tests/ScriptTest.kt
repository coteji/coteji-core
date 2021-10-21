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

import io.github.coteji.runner.evaluateScript
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test
import java.io.File
import javax.script.ScriptException

class ScriptTest {

    @Test
    fun `test DependsOn`() {
        val coteji = evaluateScript(File("src/test/resources/testing.coteji.kts"))
        assertThrowsExactly(UninitializedPropertyAccessException::class.java) { coteji.testsSource }
        assertThrowsExactly(UninitializedPropertyAccessException::class.java) { coteji.testsTarget }
    }

    @Test
    fun `invalid script`() {
        val errors = mutableListOf<String>()
        try {
            evaluateScript(File("src/test/resources/invalid.coteji.kts")) { errors.add(it.message) }
        } catch (e: ScriptException) {
            // ignore
        }
        assertThat(errors).isNotEmpty
    }

}