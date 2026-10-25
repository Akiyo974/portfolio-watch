package com.christendijoux.watch.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.christendijoux.watch.R
import com.christendijoux.watch.data.contactCard
import com.christendijoux.watch.data.qrMatrix
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Paper

/** Zone blanche autour du code, en modules : 4 selon la norme QR. */
private const val QUIET_ZONE = 4

/** Ma carte de visite en QR code : un téléphone la scanne pour m'ajouter à ses contacts. */
@Composable
fun QrScreen() {
    val jobTitle = stringResource(R.string.card_job_title)
    val matrix = remember(jobTitle) { qrMatrix(contactCard(jobTitle)) }
    val description = stringResource(R.string.qr_description)

    KeepScreenBright()

    BoxWithConstraints(Modifier.fillMaxSize().background(Ink)) {
        // Presque le plus grand carré inscrit dans l'écran rond (côté = diamètre / √2) :
        // seuls les coins de la zone blanche touchent le bord, le code reste entier
        val side = maxWidth * 0.7f

        Canvas(
            Modifier
                .align(Alignment.Center)
                .size(side)
                .semantics { contentDescription = description },
        ) {
            val cells = matrix.width + QUIET_ZONE * 2
            val cell = size.width / cells
            drawRoundRect(color = Paper, cornerRadius = CornerRadius(cell * 2))
            for (y in 0 until matrix.height) {
                for (x in 0 until matrix.width) {
                    if (matrix[x, y]) {
                        drawRect(
                            color = Ink,
                            topLeft = Offset((x + QUIET_ZONE) * cell, (y + QUIET_ZONE) * cell),
                            // Léger chevauchement pour éviter les filets clairs entre modules voisins
                            size = Size(cell + 0.5f, cell + 0.5f),
                        )
                    }
                }
            }
        }
    }
}

/** Luminosité au maximum et écran allumé tant que le code est affiché, pour faciliter le scan. */
@Composable
private fun KeepScreenBright() {
    val activity = LocalContext.current as? Activity
    DisposableEffect(activity) {
        val window = activity?.window ?: return@DisposableEffect onDispose {}
        val previous = window.attributes.screenBrightness
        window.attributes = window.attributes.apply {
            screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window.attributes = window.attributes.apply { screenBrightness = previous }
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
