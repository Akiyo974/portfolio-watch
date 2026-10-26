package com.christendijoux.watch

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import androidx.wear.ambient.AmbientLifecycleObserver
import com.christendijoux.watch.ui.Shortcuts
import com.christendijoux.watch.ui.WearApp
import com.christendijoux.watch.util.AppLanguage
import com.christendijoux.watch.util.CurrentCity
import kotlinx.coroutines.launch

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

    /** Autorisation de localisation, pour la ville du cadran : demandée une seule fois. */
    private val locationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) refreshCity()
    }

    // La langue choisie dans l'app prime sur celle de la montre
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLanguage.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(ambientObserver)
        // Après une recréation (changement de langue), l'intent d'origine ne doit pas rouvrir le raccourci
        if (savedInstanceState == null) shortcut.value = intent.getStringExtra(Shortcuts.EXTRA)
        askLocationOnce()
        setContent {
            WearApp(
                shortcut = shortcut.value,
                onShortcutHandled = { shortcut.value = null },
                ambient = ambient.value,
            )
        }
    }

    // L'app au premier plan peut toujours obtenir la position : on en profite pour mettre la ville à jour
    override fun onResume() {
        super.onResume()
        refreshCity()
    }

    private fun refreshCity() {
        if (!CurrentCity.hasPermission(this)) return
        lifecycleScope.launch {
            CurrentCity.refresh(this@MainActivity)
            CurrentCity.requestComplicationUpdate(this@MainActivity)
        }
    }

    private fun askLocationOnce() {
        val prefs = getSharedPreferences("permissions", MODE_PRIVATE)
        if (CurrentCity.hasPermission(this) || prefs.getBoolean("location_asked", false)) return
        prefs.edit().putBoolean("location_asked", true).apply()
        locationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    // App déjà ouverte (launchMode singleTask) : la tuile arrive ici
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        shortcut.value = intent.getStringExtra(Shortcuts.EXTRA)
    }
}
