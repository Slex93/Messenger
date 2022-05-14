plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 23
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isTestCoverageEnabled = true
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":resources"))

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")

    /*Work Manager*/
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    /*Serialization*/
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    /*Paging*/
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")

    /*Dagger 2*/
    implementation("com.google.dagger:dagger:2.41")
    implementation("com.google.dagger:dagger-android:2.41")
    implementation("com.google.dagger:dagger-android-support:2.41")
    kapt("com.google.dagger:dagger-compiler:2.41")
    kapt("com.google.dagger:dagger-android-processor:2.41")

    /*Firebase*/
    implementation(platform("com.google.firebase:firebase-bom:28.4.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.firebaseui:firebase-ui-database:8.0.1")
    implementation("com.google.android.gms:play-services-gcm:17.0.0")
    kapt("com.google.firebase:firebase-auth:21.0.3")

    /*Glide*/
    implementation("com.github.bumptech.glide:glide:4.13.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    /*Drawer Layout*/
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")

    /*Navigation Component*/
    val navVersion = "2.4.2"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    /*Coroutines*/
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1-native-mt")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.7.0-alpha01")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}