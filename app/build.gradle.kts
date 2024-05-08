plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.ta_mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ta_mobile"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.firebase.messaging)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //fragment
    implementation(libs.androidx.fragment.ktx)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
//    implementation(libs.androidx.navigation.safe.args.gradle.plugin)

    //data store
    implementation(libs.androidx.datastore.preferences)

    //room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //splash screen
    implementation (libs.androidx.core.splashscreen)

    //glide
    implementation(libs.glide)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

    //interceptor
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.navigation)

    //maps
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    //carousel
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    //rating
    implementation("com.github.unaisulhadi:emojiratingbar:1.0.5")

    //chart
    implementation("com.github.AnyChart:AnyChart-Android:1.1.5")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("io.github.dzmitry-lakisau:month-year-picker-dialog:1.0.0")

}