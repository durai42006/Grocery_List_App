plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}


android {
    namespace = "com.example.grozon"
    compileSdk = 34

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

    implementation(libs.androidx.core.ktx)
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

    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.12.0")

    implementation("com.airbnb.android:lottie:3.4.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")


//    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
//    implementation("de.codecrafters.tableview:tableview:2.8.0")


    implementation(libs.itext7.core)
    implementation("org.apache.poi:poi:5.3.0")
    implementation("org.apache.poi:poi-ooxml:5.3.0")

    implementation("com.itextpdf:itext7-core:7.2.3")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation ("com.itextpdf:itext7-core:7.1.15")
    implementation ("com.itextpdf:layout:7.1.15")
    implementation ("com.itextpdf:io:7.1.15")

//    implementation(libs.recyclerview)
//    implementation(libs.viewpager2)
//    implementation(libs.androidPdfViewer)
//    implementation (libs.androidPdfViewerBeta)



}