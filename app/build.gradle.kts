import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.vietcuisine"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.vietcuisine"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
      buildFeatures {
        viewBinding = true
    }
    
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.viewpager2)
    implementation(libs.swiperefreshlayout)

    
    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    
    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
      // Image loading
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)    // Video player
    implementation(libs.media3)
    implementation(libs.media3.ui)
    
    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    
    // Fragment support
    implementation("androidx.fragment:fragment:1.6.2")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Socket
    implementation(libs.socketio)
}