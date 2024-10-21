plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Asegúrate de incluir este plugin
}

android {
    namespace = "com.example.hotelcielo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hotelcielo"
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
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // Usamos el BOM de Firebase para garantizar la compatibilidad de versiones
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    // Excluir firebase-common de firestore
    implementation("com.google.firebase:firebase-firestore")

    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-functions")

    // Otras dependencias
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.android.volley:volley:1.2.1")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    // Dependencias de prueba

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
