plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pcsurmesure"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pcsurmesure"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    packagingOptions {
        resources {
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENSE.md" // You can add more if there are other conflicts
        }
    }
//    packaging {
//            resources {
//                excludes += setOf(
//                    "META-INF/LICENSE.md",
//                    "META-INF/LICENSE",
//                    "META-INF/NOTICE",
//                    "META-INF/NOTICE.md"
//                )
//            }
//        }
}



dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.database)
    implementation("com.google.firebase:firebase-firestore:24.6.0")
    implementation("com.google.firebase:firebase-auth:22.0.0")
//    implementation(libs.junit.jupiter)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
//    testImplementation(libs.testng)
    testImplementation(libs.ext.junit)
    testImplementation(libs.ext.junit)
    api("org.apache.commons:commons-lang3:3.11")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("org.robolectric:robolectric:4.10.2")
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    testImplementation("org.mockito:mockito-core:3.9.0")
    testImplementation("org.mockito:mockito-inline:3.9.0")
}

tasks.withType<Test> {
    useJUnitPlatform() // This is required for JUnit 5
}
//java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(11))
//    }
//}