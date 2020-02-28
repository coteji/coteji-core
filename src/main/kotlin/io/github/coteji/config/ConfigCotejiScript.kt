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

package io.github.coteji.config

import io.github.coteji.core.CotejiBuilder
import org.jetbrains.kotlin.script.util.DependsOn
import org.jetbrains.kotlin.script.util.Import
import org.jetbrains.kotlin.script.util.Repository
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
        displayName = "Coteji configuration script",
        fileExtension = "coteji.kts",
        compilationConfiguration = CotejiKtsScriptDefinition::class
)
abstract class ConfigCotejiScript {
    fun scriptBody() {
    }
}

object CotejiKtsScriptDefinition : ScriptCompilationConfiguration(
        {
            defaultImports(
                    "org.jetbrains.kotlin.script.util.*",
                    "io.github.coteji.core.*",
                    "io.github.coteji.config.*"
            )

            implicitReceivers(CotejiBuilder::class)

            refineConfiguration {
                onAnnotations(DependsOn::class, Repository::class, Import::class, handler = AnnotationSupportScriptConfigurator())
            }

            jvm {
                dependenciesFromClassloader(wholeClasspath = true)
            }

            ide {
                acceptedLocations(ScriptAcceptedLocation.Everywhere)
            }
        })