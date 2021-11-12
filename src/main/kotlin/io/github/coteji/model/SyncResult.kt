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
package io.github.coteji.model

class SyncResult {
    var testsFoundInSourceCount: Int = 0
    lateinit var testsWithoutId: List<CotejiTest>
    lateinit var pushResult: PushResult

    override fun toString(): String {
        return """
Tests found in the Source: ${testsFoundInSourceCount}
Tests without ID: ${testsWithoutId.size}
$pushResult
        """.trimIndent()
    }
}
