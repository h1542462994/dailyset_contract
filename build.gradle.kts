import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinSerializationVersion: String by extra { "1.3.3" }
val kotlinSerializationPluginVersion: String by extra { "1.6.10" }
val kotlinCoroutinesVersion: String by extra { "1.6.1" }

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "org.tty.dailyset"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // kotlinx
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${kotlinSerializationVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinCoroutinesVersion}")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

