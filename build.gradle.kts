import java.net.URI

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.2")
        classpath("com.google.gms:google-services:4.3.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI("https://jitpack.io")
        }
    }
}

tasks.register(name = "type", type = Delete::class) {
    delete(rootProject.buildDir)
}