plugins {
    alias(libs.plugins.android.application)
}

// Cadran au Watch Face Format : aucun code, seulement des ressources (res/raw/watchface.xml)
android {
    namespace = "com.christendijoux.watch.watchface"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.christendijoux.watch.watchface"
        // Watch Face Format v1 : Wear OS 4+ (API 33)
        minSdk = 33
        targetSdk = 35
        versionCode = 2
        versionName = "0.6.0"
    }

    // Même clé fixe que l'app (voir app/build.gradle.kts)
    val signingStore = System.getenv("SIGNING_STORE_FILE")?.let { file(it) }
    signingConfigs {
        if (signingStore != null) {
            create("release") {
                storeFile = signingStore
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            if (signingStore != null) signingConfig = signingConfigs.getByName("release")
            // Pas de code à réduire ; R8 retirerait des ressources que seul watchface.xml référence
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}
