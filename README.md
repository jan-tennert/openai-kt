# openai-kt

A Kotlin Multiplatform SDK for OpenAI's APIs.
Supported targets:
- JVM
- Android
- JS (Browser)

Newest stable version: ![stable](https://img.shields.io/github/release/jan-tennert/openai-kt?label=stable)

Newest experimental version: ![experimental](https://img.shields.io/maven-central/v/io.github.jan-tennert/openai-kt?label=experimental)

# Installation

(Currently N/A)

```kotlin
dependencies {
    implementation("io.github.jan-tennert:openai-kt:VERSION")

    //add ktor client engine (if you don't already have one, see https://ktor.io/docs/http-client-engines.html for all engines)
    //e.g. the CIO engine
    implementation("io.ktor:ktor-client-cio:KTOR_VERSION")
}
```