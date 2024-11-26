plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}



android {
    namespace = "com.example.grozon"
    compileSdk = 34



        testOptions {
            unitTests.isIncludeAndroidResources = true
        }


    defaultConfig {
        applicationId = "com.example.grozon"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

//    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.drawerlayout)
//    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation(libs.glide)
    implementation(libs.okhttp3.integration)

//    implementation (libs.android.pdf.viewer)
//    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.1")



    implementation(libs.lottie)

    implementation ("de.hdodenhof:circleimageview:3.1.0")


//    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
//    implementation("de.codecrafters.tableview:tableview:2.8.0")

    implementation("androidx.core:core-ktx:1.12.0")



    implementation(libs.itext7.core)
    implementation("org.apache.poi:poi:5.3.0")
    implementation("org.apache.poi:poi-ooxml:5.3.0")

    implementation("com.itextpdf:itext7-core:7.2.3")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
//    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation ("com.itextpdf:itext7-core:7.1.15")
    implementation ("com.itextpdf:layout:7.1.15")
    implementation ("com.itextpdf:io:7.1.15")
    implementation ("com.github.barteksc:pdfium-android:1.9.0")
//    implementation ("com.github.mhiew:android-pdf-viewer:3.2.0-beta.1")



//    implementation ("androidx.recyclerview:recyclerview:1.3.0")
//    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")

}