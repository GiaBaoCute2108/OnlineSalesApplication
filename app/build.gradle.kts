@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.appbanhangonline"
    compileSdk = 34

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
    }

    defaultConfig {
        applicationId = "com.example.appbanhangonline"
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(fileTree(mapOf(
        "dir" to "F:\\zalopay",
        "include" to listOf("*.aar", "*.jar")
    )))
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.github.bumptech.glide:glide:4.16.0")

    //RxJava
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    //badge
    implementation ("com.nex3z:notification-badge:1.0.4")

    //eventbus
    implementation("org.greenrobot:eventbus:3.2.0")

    //paper
    implementation("io.github.pilgr:paperdb:2.7.1")

    //gson
    implementation("com.google.code.gson:gson:2.8.9")

    //lottie
    implementation ("com.airbnb.android:lottie:6.4.0")

    //neumorphism
    implementation("com.github.fornewid:neumorphism:0.3.2")

    //imagepicker
    implementation("com.github.dhaval2404:imagepicker:2.1")

    //firebase
    implementation("com.google.firebase:firebase-auth:23.0.0")

    //firebase cloud messagging
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("com.google.firebase:firebase-analytics:22.0.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")

    //OAuth 2.0
    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")

    //Momo
    implementation("com.github.momo-wallet:mobile-sdk:1.0.7")

    implementation ("androidx.core:core:1.3.2")

    implementation("com.squareup.okhttp3:okhttp:4.6.0")
    implementation("commons-codec:commons-codec:1.14")

    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.0.0")

    //ImageSlideShow
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    implementation ("live.videosdk:rtc-android-sdk:0.1.21")

    // library to perform Network call to generate a meeting id
    implementation ("com.amitshekhar.android:android-networking:1.0.2")

    implementation("com.google.android.exoplayer:exoplayer:2.18.5")

    implementation("com.github.Spikeysanju:MotionToast:1.4")
}