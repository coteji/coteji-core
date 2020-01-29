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

package org.coteji.sources

import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.MemberValuePair
import java.util.*

class MethodCondition {
    lateinit var condition: MethodDeclaration.() -> Boolean
    lateinit var annotation: MethodDeclaration.() -> Optional<AnnotationExpr>
    lateinit var attribute: AnnotationExpr.() -> Optional<MemberValuePair>

    fun withAnnotation(name: String) {
        annotation = { this.findFirst(AnnotationExpr::class.java) { it.nameAsString == name } }
        condition = { this.annotation().isPresent }
    }

    fun thatHasAttribute(name: String) {
        attribute = { this.findFirst(MemberValuePair::class.java) { it.nameAsString == name } }
        condition = {
            var result = false
            if (this.annotation().isPresent) {
                result = this.annotation().get().attribute().isPresent
            }
            result
        }
    }

    fun thatContainsText(text: String) {
        condition = {
            var result = false
            if (this.annotation().isPresent) {
                if (this.annotation().get().attribute().isPresent) {
                    result = this.annotation().get().attribute().get().value.toString().contains(text)
                }
            }
            result
        }
    }
}

fun method(init: MethodCondition.() -> Unit): MethodDeclaration.() -> Boolean {
    val methodCondition = MethodCondition()
    methodCondition.init()
    return methodCondition.condition
}