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
package io.github.coteji.tests.targets

import io.github.coteji.model.Test
import io.github.coteji.core.TestsTarget

class FakeTarget : TestsTarget {
    override fun push(test: Test) {
        println("Test pushed: $test")
    }

    override fun pushAll(tests: List<Test>) {
        tests.forEach {
            println(it.toString())
        }
        println("All tests pushed")
    }
}