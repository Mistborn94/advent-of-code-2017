import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.10"
    id("org.junit.platform.gradle.plugin") version "1.0.2"
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    testCompile("org.junit.jupiter:junit-jupiter-engine:5.0.2")
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}