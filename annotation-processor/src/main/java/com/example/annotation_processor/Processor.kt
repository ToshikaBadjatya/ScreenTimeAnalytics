package com.example.annotation_processor

import com.example.annotations.TrackActivity
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassWriter
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class Processor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(TrackActivity::class.java.canonicalName)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment
    ): Boolean {
        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: return false
        roundEnv.getElementsAnnotatedWith(TrackActivity::class.java).forEach {
            val packageName = processingEnv.elementUtils.getPackageOf(it).toString()
            val interfaceName = it.simpleName.toString()
            val className = "Track${interfaceName}"
            val typeSpec = TypeSpec.classBuilder(ClassName(packageName, className)).build()
            FileSpec.builder(packageName, className)
                .addType(typeSpec).build()
                .writeTo(File(kaptKotlinGeneratedDir))
        }
        return true
    }

}