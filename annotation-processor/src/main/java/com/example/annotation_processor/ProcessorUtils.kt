package com.example.annotation_processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

object ProcessorUtils {
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