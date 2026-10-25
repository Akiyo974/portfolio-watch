package com.christendijoux.watch.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.christendijoux.watch.R
import java.util.concurrent.Executors

private val executor = Executors.newSingleThreadExecutor()

/**
 * Ouvre un lien sur le téléphone associé (la montre n'a pas de vrai navigateur).
 * Fonctionne avec les liens http(s), mailto: et tel:. La confirmation n'apparaît qu'une fois
 * la réponse du téléphone reçue, pour ne jamais annoncer une ouverture qui a échoué.
 */
fun Context.openOnPhone(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        .addCategory(Intent.CATEGORY_BROWSABLE)
    val future = RemoteActivityHelper(applicationContext, executor).startRemoteActivity(intent)
    future.addListener(
        {
            val opened = runCatching { future.get() }.isSuccess
            if (opened) {
                showConfirmation(ConfirmationActivity.OPEN_ON_PHONE_ANIMATION, R.string.phone_opened)
            } else {
                showConfirmation(ConfirmationActivity.FAILURE_ANIMATION, R.string.phone_failed)
            }
        },
        ContextCompat.getMainExecutor(this),
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

/** Animation de confirmation standard de Wear OS (remplace les Toast, déconseillés sur montre). */
private fun Context.showConfirmation(animation: Int, @StringRes messageRes: Int) {
    // La réponse du téléphone peut arriver après la fermeture de l'écran
    if (this is Activity && (isFinishing || isDestroyed)) return
    val intent = Intent(this, ConfirmationActivity::class.java)
        .putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animation)
        .putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(messageRes))
    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
