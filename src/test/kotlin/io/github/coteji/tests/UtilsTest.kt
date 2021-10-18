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

import io.github.coteji.model.CotejiTest
import io.github.coteji.model.PushResult
import io.github.coteji.model.SyncResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UtilsTest {
    @Test
    fun `test to String`() {
        val inputTest = CotejiTest(
            id = "0",
            name = "someUser",
            content = "Line 1\nLine 2",
            attributes = mapOf(Pair("component", "users"), Pair("type", "ui"))
        )
        assertThat(inputTest.toString()).isEqualTo(
            "\nName: someUser\nID: 0" +
                    "\nContent:\n\tLine 1\n\tLine 2" +
                    "\nAttributes:\n\tcomponent = users\n\ttype = ui"
        )

    }

    @Test
    fun `test to String no ID`() {
        val inputTest = CotejiTest(
            name = "someUser",
            content = "Line 1\nLine 2",
            attributes = mapOf(Pair("component", "users"), Pair("type", "ui"))
        )
        assertThat(inputTest.toString()).isEqualTo(
            "\nName: someUser\nID: no ID" +
                    "\nContent:\n\tLine 1\n\tLine 2" +
                    "\nAttributes:\n\tcomponent = users\n\ttype = ui"
        )

    }

    @Test
    fun `sync result to String`() {
        val test1 = CotejiTest(
            name = "createUser",
            content = "some content",
            attributes = mapOf()
        )
        val test2 = CotejiTest(
            id = "0",
            name = "updateUser",
            content = "some content updated",
            attributes = mapOf()
        )

        val syncResult = SyncResult()
        syncResult.testsFoundInSourceCount = 2
        syncResult.testsWithoutId = listOf(test1)
        val pushResult = PushResult()
        pushResult.testsAdded.addAll(listOf(test1, test2))
        pushResult.testsUpdated.addAll(listOf(test2))
        pushResult.testsDeleted.addAll(listOf("1", "2", "3"))
        pushResult.testsAlreadyUpToDate.addAll(listOf(test1))
        pushResult.testsWithNonExistingId.addAll(listOf(test1, test2))
        pushResult.testsSyncFailed[test1] = "error"
        syncResult.pushResult = pushResult
        assertThat(syncResult.toString()).isEqualTo(
            "Tests found in the Source: 2\n" +
                    "Tests without ID: 1\n" +
                    "Tests with non-existing ID: 2\n" +
                    "Tests added: 2\n" +
                    "Tests updated: 1\n" +
                    "Tests already up to date: 1\n" +
                    "Tests deleted: 3\n" +
                    "Tests failed to synchronize: 1"
        )

    }
}