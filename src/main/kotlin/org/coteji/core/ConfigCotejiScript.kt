package org.coteji.core

import org.jetbrains.kotlin.script.util.DependsOn
import org.jetbrains.kotlin.script.util.Import
import org.jetbrains.kotlin.script.util.Repository
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
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
                    "org.coteji.core.*"
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