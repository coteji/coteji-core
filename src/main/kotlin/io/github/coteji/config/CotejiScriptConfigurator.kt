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

import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.dependencies.*
import kotlin.script.experimental.jvm.updateClasspath

class CotejiScriptConfigurator : RefineScriptCompilationConfigurationHandler {

    private val resolver = CompoundDependenciesResolver(FileSystemDependenciesResolver())

    override operator fun invoke(context: ScriptConfigurationRefinementContext): ResultWithDiagnostics<ScriptCompilationConfiguration> {
        val diagnostics = arrayListOf<ScriptDiagnostic>()

        val annotations = context.collectedData?.get(ScriptCollectedData.foundAnnotations)?.takeIf { it.isNotEmpty() }
            ?: return context.compilationConfiguration.asSuccess()

        val resolveResult = try {
            runBlocking {
                resolveFromAnnotations(resolver, annotations.filter { it is DependsOn || it is Repository })
            }
        } catch (e: Throwable) {
            ResultWithDiagnostics.Failure(
                *diagnostics.toTypedArray(),
                e.asDiagnostics(path = context.script.locationId)
            )
        }

        return resolveResult.onSuccess { resolvedClassPath ->
            ScriptCompilationConfiguration(context.compilationConfiguration) {
                updateClasspath(resolvedClassPath)
            }.asSuccess()
        }
    }
}

suspend fun resolveFromAnnotations(
    resolver: ExternalDependenciesResolver,
    annotations: Iterable<Annotation>
): ResultWithDiagnostics<List<File>> {
    val reports = mutableListOf<ScriptDiagnostic>()
    annotations.forEach { annotation ->
        when (annotation) {
            is Repository -> {
                for (coordinates in annotation.repositoriesCoordinates) {
                    val added = resolver.addRepository(coordinates)
                        .also { reports.addAll(it.reports) }
                        .valueOr { return it }

                    if (!added)
                        return reports + makeFailureResult(
                            "Unrecognized repository coordinates: $coordinates"
                        )
                }
            }
            is DependsOn -> {
            }
            else -> return makeFailureResult("Unknown annotation ${annotation.javaClass}")
        }
    }
    return annotations.filterIsInstance(DependsOn::class.java).flatMapSuccess { annotation ->
        annotation.artifactsCoordinates.asIterable().flatMapSuccess { artifactCoordinates ->
            resolver.resolve(artifactCoordinates)
        }
    }
}
