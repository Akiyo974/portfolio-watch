package com.christendijoux.watch

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.christendijoux.watch.ui.WearApp
import com.christendijoux.watch.util.AppLanguage

class MainActivity : ComponentActivity() {
    // La langue choisie dans l'app prime sur celle de la montre
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLanguage.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WearApp() }
    }
}
