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
package io.github.coteji.model

data class CotejiTest(
        val id: String? = null,
        val name: String,
        val content: String = "",
        val attributes: Map<String, Any> = hashMapOf()
) {
    override fun toString(): String {
        var result =  "\nName: $name\nID: ${id ?: "no ID"}\nContent:"
        content.lines().forEach { result += "\n\t$it" }
        result += "\nAttributes:"
        attributes.entries.forEach { result += "\n\t${it.key} = ${it.value}" }
        return result
    }
}