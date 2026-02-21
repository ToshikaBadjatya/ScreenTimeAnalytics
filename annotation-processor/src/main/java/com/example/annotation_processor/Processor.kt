package com.example.annotation_processor

import com.example.annotations.TrackActivity
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
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
        roundEnv.getElementsAnnotatedWith(TrackActivity::class.java).forEachIndexed {index, it ->
            val packageName = processingEnv.elementUtils.getPackageOf(it).toString()
            val interfaceName = it.simpleName.toString()
            if(index==0){
               generateBaseActivity(packageName,kaptKotlinGeneratedDir)
            }


        }
        return true
    }
    fun generateBaseActivity(packageName: String,kaptKotlinGeneratedDir: String){
        val className = "ScreenTimeActivity"
        FileSpec.builder(packageName, className)
            .addType(getTypeSpec(className))
            .build()
            .writeTo(File(kaptKotlinGeneratedDir))

    }
    fun getTypeSpec(className: String): TypeSpec {

        // ---- Correct ClassNames (single source of truth) ----
        val eventClass = ClassName(
            "com.example.screentimeanalytics.storage.event",
            "Event"
        )

        val analyticsScopeClass = ClassName(
            "com.example.screentimeanalytics.utils",
            "AnalyticsScope"
        )

        val screenTimeAnalyticsClass = ClassName(
            "com.example.screentimeanalytics",
            "ScreenTimeAnalytics"
        )

        // ---- Property: event ----
        val eventProperty = PropertySpec.builder(
            "event",
            eventClass.nestedClass("Builder").copy(nullable = true)
        )
            .addModifiers(KModifier.PRIVATE)
            .mutable(true)
            .initializer("null")
            .build()

        // ---- onResume ----
        val onResumeFun = FunSpec.builder("onResume")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("super.onResume()")
            .addStatement(
                "event = %T.Builder(getClassName()).apply { start() }",
                eventClass
            )
            .build()

        // ---- onPause ----
        val onPauseFun = FunSpec.builder("onPause")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("super.onPause()")
            .beginControlFlow("if (event == null)")
            .addStatement("return")
            .endControlFlow()
            .addStatement("event!!.stop()")
            .beginControlFlow("%T.scope.%M", analyticsScopeClass, MemberName("kotlinx.coroutines", "launch"))            .beginControlFlow("%T.analytics?.let", screenTimeAnalyticsClass)
            .addStatement("it.logEvent(event!!.build())")
            .addStatement("event = null")
            .endControlFlow()
            .endControlFlow()
            .build()

        // ---- onDestroy ----
        val onDestroyFun = FunSpec.builder("onDestroy")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("super.onDestroy()")
            .beginControlFlow("if (event != null && event!!.isRunning())")
            .addStatement("event!!.stop()")
            .beginControlFlow("%T.scope.%M", analyticsScopeClass,
                MemberName("kotlinx.coroutines", "launch")
            )            .beginControlFlow("%T.analytics?.let", screenTimeAnalyticsClass)
            .addStatement("it.logEvent(event!!.build())")
            .addStatement("event = null")
            .endControlFlow()
            .endControlFlow()
            .endControlFlow()
            .build()

        // ---- getClassName ----
        val getClassNameFun = FunSpec.builder("getClassName")
            .returns(String::class)
            .addStatement("return this::class.simpleName ?: \"\"")
            .build()

        // ---- TypeSpec ----
        return TypeSpec.classBuilder(className)
            .addModifiers(KModifier.OPEN)
            .superclass(ClassName("androidx.appcompat.app", "AppCompatActivity"))
            .addProperty(eventProperty)
            .addFunction(onResumeFun)
            .addFunction(onPauseFun)
            .addFunction(onDestroyFun)
            .addFunction(getClassNameFun)
            .build()
    }

}