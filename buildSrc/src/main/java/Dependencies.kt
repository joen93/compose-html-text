import org.gradle.api.JavaVersion

object Plugins {
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"
    const val KOTLIN_ANDROID = "kotlin-android"
    const val MAVEN_PUBLISH = "maven-publish"
}

object Config {
    const val GROUP_ID = "io.joen.compose"
    const val ARTIFACT_ID = "html-text"
    const val VERSION_NAME = "0.1.0-alpha03"

    const val MIN_SDK = 21
    const val COMPILE_SDK = 31
    const val TARGET_SDK = 31

    val javaVersion = JavaVersion.VERSION_1_8
    const val JVM_TARGET = "1.8"
}

object Versions {
    const val AGP = "7.0.3"
    const val KOTLIN = "1.5.31"

    const val ANDROIDX_CORE = "1.6.0"

    const val COMPOSE = "1.1.0-beta01"
}

object Libraries {
    const val KOTLIN_STANDARD_LIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"

    const val ANDROIDX_CORE = "androidx.core:core-ktx:${Versions.ANDROIDX_CORE}"

    const val COMPOSE_FOUNDATION = "androidx.compose.foundation:foundation:${Versions.COMPOSE}"
    const val COMPOSE_UI = "androidx.compose.ui:ui:${Versions.COMPOSE}"
    const val COMPOSE_MATERIAL = "androidx.compose.material:material:${Versions.COMPOSE}"
    const val COMPOSE_UI_TOOLING = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE}"
}
