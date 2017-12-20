import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.10"
    id("org.junit.platform.gradle.plugin") version "1.0.2"
}

val junitVersion = "5.0.2"
dependencies {
    compile(kotlin("stdlib-jdk8"))
    testCompile(junit("junit-jupiter-engine"))
    testCompile(junit("junit-jupiter-api"))
    testCompile(junit("junit-jupiter-params"))
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

fun Build_gradle.junit(module: String) = "org.junit.jupiter:$module:$junitVersion"