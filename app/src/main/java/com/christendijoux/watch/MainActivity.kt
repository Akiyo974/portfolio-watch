package com.christendijoux.watch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.wear.ambient.AmbientLifecycleObserver
import com.christendijoux.watch.ui.Shortcuts
import com.christendijoux.watch.ui.WearApp
import com.christendijoux.watch.util.AppLanguage

class MainActivity : ComponentActivity() {
    /** Raccourci demandé par la tuile, en attente d'être ouvert. */
    private val shortcut = mutableStateOf<String?>(null)

    /** Mode veille (écran toujours allumé) : l'app reste affichée au lieu de revenir au cadran. */
    private val ambient = mutableStateOf(false)

    private val ambientObserver = AmbientLifecycleObserver(
        this,
        object : AmbientLifecycleObserver.AmbientLifecycleCallback {
            override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
                ambient.value = true
            }

            override fun onExitAmbient() {
                ambient.value = false
            }

            // L'heure de l'écran de veille se met à jour d'elle-même (ACTION_TIME_TICK)
            override fun onUpdateAmbient() = Unit
        },
    )

    // La langue choisie dans l'app prime sur celle de la montre
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLanguage.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(ambientObserver)
        // Après une recréation (changement de langue), l'intent d'origine ne doit pas rouvrir le raccourci
        if (savedInstanceState == null) shortcut.value = intent.getStringExtra(Shortcuts.EXTRA)
        setContent {
            WearApp(
                shortcut = shortcut.value,
                onShortcutHandled = { shortcut.value = null },
                ambient = ambient.value,
            )
        }
    }

    // App déjà ouverte (launchMode singleTask) : la tuile arrive ici
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        shortcut.value = intent.getStringExtra(Shortcuts.EXTRA)
    }
}
