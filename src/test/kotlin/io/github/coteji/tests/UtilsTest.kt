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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UtilsTest {
    @Test
    fun testToString() {
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
    fun testToStringNoId() {
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
}