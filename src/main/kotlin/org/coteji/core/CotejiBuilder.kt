package org.coteji.core

class CotejiBuilder {
    lateinit var testsSource: TestsSource
    lateinit var testsTarget: TestsTarget

    fun build() {
        testsTarget.pushAll(testsSource.getAllTests())
    }

}

fun CotejiBuilder.setSource(source: TestsSource) {
    testsSource = source
}

fun CotejiBuilder.setTarget(target: TestsTarget) {
    testsTarget = target
}