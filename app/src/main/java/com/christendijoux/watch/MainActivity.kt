package com.christendijoux.watch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.christendijoux.watch.ui.Shortcuts
import com.christendijoux.watch.ui.WearApp
import com.christendijoux.watch.util.AppLanguage

class MainActivity : ComponentActivity() {
    /** Raccourci demandé par la tuile, en attente d'être ouvert. */
    private val shortcut = mutableStateOf<String?>(null)

    // La langue choisie dans l'app prime sur celle de la montre
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLanguage.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Après une recréation (changement de langue), l'intent d'origine ne doit pas rouvrir le raccourci
        if (savedInstanceState == null) shortcut.value = intent.getStringExtra(Shortcuts.EXTRA)
        setContent {
            WearApp(shortcut = shortcut.value, onShortcutHandled = { shortcut.value = null })
        }
    }

    // App déjà ouverte (launchMode singleTask) : la tuile arrive ici
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        shortcut.value = intent.getStringExtra(Shortcuts.EXTRA)
    }
}
