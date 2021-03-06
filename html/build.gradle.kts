plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.MAVEN_PUBLISH)
}

android {
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK

        consumerProguardFile("consumer-rule.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }

    kotlinOptions.jvmTarget = Config.JVM_TARGET
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.COMPOSE
}

dependencies {
    implementation(Libraries.KOTLIN_STANDARD_LIB)
    implementation(Libraries.ANDROIDX_CORE)
    implementation(Libraries.COMPOSE_FOUNDATION)
    implementation(Libraries.COMPOSE_UI)
    implementation(Libraries.COMPOSE_MATERIAL)
    implementation(Libraries.COMPOSE_UI_TOOLING)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.getByName("release"))
                groupId = Config.GROUP_ID
                artifactId = Config.ARTIFACT_ID
                version = Config.VERSION_NAME
            }
        }
    }
}
