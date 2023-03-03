plugins {
    kotlin("multiplatform") version Versions.KOTLIN
    id("com.android.library")
    kotlin("plugin.serialization") version Versions.KOTLIN
    `maven-publish`
}

group = "io.github.jan-tennert"
version = "0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(8)
    }
    js(IR) {
        browser()
        nodejs()
    }
    android {
        publishLibraryVariants("release", "debug")
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}")
                api("io.ktor:ktor-client-core:${Versions.KTOR}")
                api("io.ktor:ktor-client-content-negotiation:${Versions.KTOR}")
                api("io.ktor:ktor-serialization-kotlinx-json:${Versions.KTOR}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lint {
        abortOnError = false
    }
}