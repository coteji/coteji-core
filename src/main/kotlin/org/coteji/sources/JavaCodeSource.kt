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
package org.coteji.sources

import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.utils.SourceRoot
import org.coteji.core.Test
import org.coteji.core.TestsSource
import java.io.File

class JavaCodeSource(
        private val testsDir: String,
        private val isTest: MethodDeclaration.() -> Boolean = method { withAnnotation("Test") }

) : TestsSource {

    override fun getTest(searchCriteria: String): Test {
        println("Test retrieved with filter $searchCriteria")
        return Test(name = "")
    }

    override fun getTests(searchCriteria: String): List<Test> {
        println("Tests retrieved with filter $searchCriteria")
        return emptyList()
    }

    override fun getAllTests(): List<Test> {
        val result = arrayListOf<Test>()
        val packagePath = File(testsDir).toPath()
        SourceRoot(packagePath)
                .tryToParse("")
                .filter { it.isSuccessful }
                .map { it.result.get() }
                .forEach { compilationUnit ->
                    compilationUnit.findAll(MethodDeclaration::class.java).forEach { method ->
                        if (method.isTest()) {
                            result.add(Test(name = method.nameAsString))
                        }
                    }
                }
        return result
    }

}