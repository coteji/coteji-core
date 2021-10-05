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

package io.github.coteji.config

import io.github.coteji.core.Coteji
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
    displayName = "Coteji configuration script",
    fileExtension = "coteji.kts",
    compilationConfiguration = CotejiKtsScriptDefinition::class
)
abstract class ConfigCotejiScript

object CotejiKtsScriptDefinition : ScriptCompilationConfiguration(
    {
        defaultImports(
            "io.github.coteji.core.*",
            "io.github.coteji.config.*",
            "kotlin.script.experimental.dependencies.DependsOn",
            "kotlin.script.experimental.dependencies.Repository"
        )

        implicitReceivers(Coteji::class)

        jvm {
            dependenciesFromClassloader(wholeClasspath = true)
        }

        ide {
            acceptedLocations(ScriptAcceptedLocation.Everywhere)
        }
    })
