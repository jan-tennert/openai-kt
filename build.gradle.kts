plugins {
    kotlin("multiplatform") version Versions.KOTLIN
    id("com.android.library")
    kotlin("plugin.serialization") version Versions.KOTLIN
    `maven-publish`
    id("org.jetbrains.dokka") version Versions.DOKKA
    id("io.codearte.nexus-staging") version Versions.NEXUS_STAGING
    signing
}

nexusStaging {
    stagingProfileId = Publishing.PROFILE_ID
    stagingRepositoryId.set(Publishing.REPOSITORY_ID)
    username = Publishing.SONATYPE_USERNAME
    password = Publishing.SONATYPE_PASSWORD
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
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

val dependsOnTasks = mutableListOf<String>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOnTasks.add(this.name.replace("publish", "sign").replaceAfter("Publication", ""))
    dependsOn(dependsOnTasks)
}

signing {
    val signingKey = providers
        .environmentVariable("GPG_SIGNING_KEY")
        .orElse(File(System.getenv("GPG_PATH") ?: "").let {
            try {
                it.readText()
            } catch(_: Exception) {
                ""
            }
        })
        .forUseAtConfigurationTime()
    val signingPassphrase = providers
        .environmentVariable("GPG_SIGNING_PASSPHRASE")
        .forUseAtConfigurationTime()

    if (signingKey.isPresent && signingPassphrase.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), signingPassphrase.get())
        val extension = extensions
            .getByName("publishing") as PublishingExtension
        sign(extension.publications)
    }
}
publishing {
    repositories {
        maven {
            name = "Oss"
            setUrl {
                "https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/${Publishing.REPOSITORY_ID}"
            }
            credentials {
                username = Publishing.SONATYPE_USERNAME
                password = Publishing.SONATYPE_PASSWORD
            }
        }
        maven {
            name = "Snapshot"
            setUrl { "https://s01.oss.sonatype.org/content/repositories/snapshots/" }
            credentials {
                username = Publishing.SONATYPE_USERNAME
                password = Publishing.SONATYPE_PASSWORD
            }
        }
    }
//val dokkaOutputDir = "H:/Programming/Other/DiscordKMDocs"
    val dokkaOutputDir = "$buildDir/dokka/openai-kt}"

    tasks.dokkaHtml {
        outputDirectory.set(file(dokkaOutputDir))
    }

    val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
        delete(dokkaOutputDir)
    }

    val javadocJar = tasks.register<Jar>("javadocJar") {
        dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaOutputDir)
    }

    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set("openai-kt")
                description.set("A Kotlin Multiplatform SDK for OpenAI APIs")
                url.set("https://github.com/jan-tenenrt/openai-kt")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://mit-license.org/")
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/jan-tenenrt/openai-kt/issues")
                }
                scm {
                    connection.set("https://github.com/jan-tenenrt/openai-kt.git")
                    url.set("https://github.com/jan-tenenrt/openai-kt")
                }
                developers {
                    developer {
                        name.set("TheRealJanGER")
                        email.set("jan.m.tennert@gmail.com")
                    }
                }
            }
        }
    }
}