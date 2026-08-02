import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    applyDefaultHierarchyTemplate()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    
    js {
        browser()
    }
    
    android {
       namespace = "com.sudhirtheindian.instagramclone.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_17
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            
            // Firebase
            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.storage)
            implementation(libs.firebase.messaging)
            
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            
            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)
            
            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutine)
            
            // Utils
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.napier)
            implementation(libs.kamel)
            implementation(libs.multiplatform.settings)
        }
        
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.moko.permissions)
        }
        
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.native)
                implementation(libs.androidx.datastore.preferences)
                implementation(libs.moko.permissions)
            }
        }
        
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.java)
                implementation(libs.sqldelight.jvm)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(libs.androidx.datastore.preferences)
            }
        }
        
        val jsMain by getting {
            dependencies {
                implementation(libs.wrappers.browser)
                implementation(libs.ktor.client.js)
                implementation(libs.sqldelight.web)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

sqldelight {
    databases {
        register("InstagramDatabase") {
            packageName.set("com.sudhirtheindian.instagramclone.db")
        }
    }
}

dependencies {
    add("androidMainImplementation", platform(libs.firebase.bom))
    androidRuntimeClasspath(libs.compose.uiTooling)
}
