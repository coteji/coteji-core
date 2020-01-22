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
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.utils.SourceRoot
import org.coteji.model.Test
import org.coteji.core.TestsSource
import java.io.File

class JavaCodeSource : TestsSource {
    private val TESTS_DIR = "D:\\Repos\\oem\\Risk-Org-Entity-System-Tests\\rcur-autotests\\tests\\cp-func-tests\\src\\test\\java\\com\\refinitiv\\qa\\tests"
    private val TEST_METHODS = "@Test"

    override fun readPropertyFile(filePath: String?) {
        println("Read file: $filePath")
    }

    override fun getTest(searchCriteria: String?): Test? {
        println("Test retrieved with filter $searchCriteria")
        return null
    }

    override fun getTests(searchCriteria: String?): List<Test?>? {
        println("Tests retrieved with filter $searchCriteria")
        return null
    }

    override fun getAllTests(): List<Test?>? {
        val result = arrayListOf<Test>()
        val packagePath = File(TESTS_DIR).toPath()
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

    private fun MethodDeclaration.isTest(): Boolean =
            this.findFirst(AnnotationExpr::class.java) { it.nameAsString == "Test" }.isPresent
}