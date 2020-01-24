/*
 * Copyright (c) 2019 Coteji AUTHORS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.coteji.targets

import org.coteji.model.Test
import org.coteji.core.TestsTarget

class JiraTarget : TestsTarget {
    override fun readPropertyFile(filePath: String?) {
        println("Read file: $filePath")
    }

    override fun push(test: Test?) {
        println("Test pushed")
    }

    override fun pushAll(tests: List<Test?>?) {
        tests?.forEach {
            println(it.toString())
        }
        println("All tests pushed")
    }
}