package com.christendijoux.watch.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.wear.remote.interactions.RemoteActivityHelper
import java.util.concurrent.Executors

private val executor = Executors.newSingleThreadExecutor()

/**
 * Ouvre un lien sur le téléphone associé (la montre n'a pas de vrai navigateur).
 * Fonctionne avec les liens http(s), mailto: et tel:. Le message n'apparaît qu'une fois
 * la réponse du téléphone reçue, pour ne jamais annoncer une ouverture qui a échoué.
 */
fun Context.openOnPhone(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        .addCategory(Intent.CATEGORY_BROWSABLE)
    val appContext = applicationContext
    val future = RemoteActivityHelper(appContext, executor).startRemoteActivity(intent)
    future.addListener(
        {
            val opened = runCatching { future.get() }.isSuccess
            val message = if (opened) "Ouvert sur votre téléphone" else "Aucun téléphone connecté"
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        },
        ContextCompat.getMainExecutor(appContext),
    )
}

/** Appel depuis la montre si elle le permet (modèle LTE ou appairée), sinon sur le téléphone. */
fun Context.call(phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        openOnPhone("tel:$phone")
    }
}
