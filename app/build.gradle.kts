plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.devtools.ksp")
}


android {
    namespace = "com.example.oop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.oop"
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
    testOptions.unitTests.isIncludeAndroidResources = true
}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation(libs.material3.android)
    implementation(libs.legacy.support.v4)
    testImplementation(libs.junit)
    testImplementation("org.robolectric:robolectric:4.12.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation(libs.ext.junit)
    implementation("com.google.code.gson:gson:2.10.1")


}