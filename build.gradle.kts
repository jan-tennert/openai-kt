import com.vanniktech.maven.publish.SonatypeHost

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

plugins {
    kotlin("multiplatform") version Versions.KOTLIN
    id("com.android.library")
    kotlin("plugin.serialization") version Versions.KOTLIN
    id("com.vanniktech.maven.publish") version "0.24.0"
  //  id("org.jetbrains.dokka") version Versions.DOKKA
}
group = "io.github.jan-tennert"

version = "0.1"

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


//publishing
mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)

    signAllPublications()

    coordinates("io.github.jan-tennert", "openai-kt", Versions.PROJECT)

    pom {
        name.set("OpenAI-KT")
        description.set("Kotlin Multiplatform SDK for OpenAI's API")
        inceptionYear.set("2023")
        url.set("https://github.com/jan-tennert/openai-kt/")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://mit-license.org/")
                distribution.set("https://mit-license.org/")
            }
        }
        developers {
            developer {
                id.set("TheRealJan")
                name.set("Jan Tennert")
                url.set("https://github.com/jan-tennert/")
            }
        }
        scm {
            url.set("https://github.com/jan-tennert/openai-kt/")
            connection.set("scm:git:git://github.com/jan-tennert/openai-kt.git")
            developerConnection.set("scm:git:ssh://git@github.com/jan-tennert/openai-kt.git")
        }
    }
}