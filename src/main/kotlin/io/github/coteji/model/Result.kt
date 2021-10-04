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

class Result {
    val testsAdded: MutableList<CotejiTest> = mutableListOf()
    val testsUpdated: MutableList<CotejiTest> = mutableListOf()
    val testsAlreadyUpToDate: MutableList<CotejiTest> = mutableListOf()
    val testsDeleted: MutableList<String> = mutableListOf()
    val testsSyncFailed: MutableMap<CotejiTest, String> = mutableMapOf()

    override fun toString(): String {
        return """
            Tests added: ${testsAdded.size}
            Tests updated: ${testsUpdated.size}
            Tests already up to date: ${testsAlreadyUpToDate.size}
            Tests deleted: ${testsDeleted.size}
            Tests failed to synchronize: ${testsSyncFailed.size}
        """.trimIndent()
    }
}