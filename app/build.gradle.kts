import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.tkjen.youtube"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tkjen.youtube"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val youtubeApiKey: String? = if (project.hasProperty("YOUTUBE_API_KEY")) {
            project.property("YOUTUBE_API_KEY") as String
        } else {
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                val props = Properties()
                props.load(localPropertiesFile.inputStream())
                props.getProperty("YOUTUBE_API_KEY")
            } else null
        }

        println(">>> [BUILD.GRADLE] YOUTUBE_API_KEY = $youtubeApiKey")

        android {
            defaultConfig {
                youtubeApiKey?.let {
                    buildConfigField("String", "YOUTUBE_API_KEY", "\"$it\"")
                }
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

val lifecycle_version = "2.9.0"
val arch_version = "2.2.0"
val fragment_version = "1.8.6"
val room_version = "2.7.1"
val hilt_version = "2.51.1"
val nav_version = "2.9.0"

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    
    //Fragment
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:$hilt_version")
    ksp("com.google.dagger:hilt-android-compiler:$hilt_version")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    //Room
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    
    //WebView
    implementation("androidx.webkit:webkit:1.8.0")
    
    //Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    
    //Material Design
    implementation("com.google.android.material:material:1.10.0")
    
    //YouTube Player
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:0.30")
    
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    
    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    //ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    
    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // SwipeRevealLayout
    implementation("com.github.rambler-digital-solutions:swipe-layout-android:1.0.17")

}