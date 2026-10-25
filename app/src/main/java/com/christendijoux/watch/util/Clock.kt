package com.christendijoux.watch.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.time.LocalDateTime

/**
 * Heure courante, mise à jour à chaque changement de minute plutôt que chaque seconde.
 * Le système envoie ACTION_TIME_TICK pile à la minute ; on rafraîchit aussi au réveil de l'écran
 * (les ticks ne sont pas envoyés écran éteint) et quand l'heure ou le fuseau changent.
 */
@Composable
fun rememberCurrentTime(): State<LocalDateTime> {
    val context = LocalContext.current
    val now = remember { mutableStateOf(LocalDateTime.now()) }

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                now.value = LocalDateTime.now()
            }
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
        // L'heure a pu changer entre la composition et l'enregistrement du récepteur
        now.value = LocalDateTime.now()
        onDispose { context.unregisterReceiver(receiver) }
    }

    return now
}
