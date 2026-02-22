package com.example.annotation_processor

import com.example.annotations.TrackActivity
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.tools.Diagnostic

class Processor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(TrackActivity::class.java.canonicalName)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment
    ): Boolean {

        roundEnv.getElementsAnnotatedWith(TrackActivity::class.java).forEach { element ->
            if (element is TypeElement) {
                // Check if the class extends ScreenTimeActivity
                if (!extendsScreenTimeActivity(element)) {
                    processingEnv.messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "@TrackActivity classes must extend ScreenTimeActivity, but ${element.simpleName} extends ${getSuperClassName(element)}",
                        element
                    )
                }
            }
        }
        return true
    }
    
    private fun getSuperClassName(element: TypeElement): String {
        val superType = element.superclass
        if (superType.kind == TypeKind.NONE) return "Object"
        val superElement = processingEnv.typeUtils.asElement(superType) as? TypeElement
        return superElement?.qualifiedName?.toString() ?: "Unknown"
    }

    private fun extendsScreenTimeActivity(element: TypeElement): Boolean {
        var current: TypeElement? = element

        while (current != null) {
            val superType = current.superclass
            if (superType.kind == TypeKind.NONE) return false

            val superElement =
                processingEnv.typeUtils.asElement(superType) as? TypeElement
                    ?: return false

            if (superElement.qualifiedName.toString() ==
                "com.example.screentimeanalytics.ScreenTimeActivity"
            ) {
                return true
            }

            current = superElement
        }
        return false
    }
}