package org.coteji.config

import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(fileExtension = "coteji.kts")
abstract class ConfigKtsScript {
    abstract fun scriptBody(): Unit
}