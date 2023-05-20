plugins {
    kotlin("multiplatform") version "1.8.21"
    id("com.android.application")
}

val frameworkVersion = "1.0.0"
val frameworkName = "KMMLibrary"

group = "me.mrsebastian"
version = frameworkVersion

repositories {
    google()
    mavenCentral()
}

kotlin {
    android()
    iosX64 { binaries.framework(frameworkName) }
    iosArm64 { binaries.framework(frameworkName) }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by creating {
            dependencies { }
        }
        val iosTest by creating {
            dependencies { }
        }
        getByName("iosArm64Main") { dependsOn(iosMain) }
        getByName("iosArm64Test") { dependsOn(iosTest) }
        getByName("iosX64Main") { dependsOn(iosMain) }
        getByName("iosX64Test") { dependsOn(iosTest) }
    }

    tasks {
        register("universalFrameworkDebug",
            org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
            baseName = frameworkName
            from(
                iosArm64().binaries.getFramework(frameworkName, "Debug"),
                iosX64().binaries.getFramework(frameworkName, "Debug")
            )
            destinationDir = buildDir.resolve("bin/universal/debug")
            group = "Universal framework"
            description = "Builds a universal (fat) debug framework"
            dependsOn("link${frameworkName}DebugFrameworkIosArm64")
            dependsOn("link${frameworkName}DebugFrameworkIosX64")
        }
        register("universalFrameworkRelease",
            org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
            baseName = frameworkName
            from(
                iosArm64().binaries.getFramework(frameworkName, "Release"),
                iosX64().binaries.getFramework(frameworkName, "Release")
            )
            destinationDir = buildDir.resolve("bin/universal/release")
            group = "Universal framework"
            description = "Builds a universal (fat) release framework"
            dependsOn("link${frameworkName}ReleaseFrameworkIosArm64")
            dependsOn("link${frameworkName}ReleaseFrameworkIosX64")
        }
        register("universalFramework") {
            dependsOn("universalFrameworkDebug")
            dependsOn("universalFrameworkRelease")
        }
    }
}

android {
    compileOptions.targetCompatibility = JavaVersion.VERSION_16
    compileOptions.sourceCompatibility = JavaVersion.VERSION_16

    namespace = "me.mrsebastian.library"
    compileSdk = 32
    defaultConfig {
        applicationId = "me.mrsebastian.library"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}