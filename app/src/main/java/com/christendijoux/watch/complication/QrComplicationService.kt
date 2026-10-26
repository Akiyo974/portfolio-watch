package com.christendijoux.watch.complication

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.MonochromaticImageComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.christendijoux.watch.MainActivity
import com.christendijoux.watch.R
import com.christendijoux.watch.ui.Shortcuts
import com.christendijoux.watch.util.AppLanguage

/**
 * Complication « Ma carte » : un toucher sur le cadran ouvre directement la carte de visite en QR code.
 * Contenu fixe : le système ne la redemande jamais (UPDATE_PERIOD_SECONDS = 0 dans le manifeste).
 */
class QrComplicationService : SuspendingComplicationDataSourceService() {

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? =
        build(request.complicationType)

    override fun getPreviewData(type: ComplicationType): ComplicationData? = build(type)

    private fun build(type: ComplicationType): ComplicationData? {
        // Même langue que celle choisie dans l'app
        val label = AppLanguage.wrap(this).getString(R.string.contact_card)
        val description = PlainComplicationText.Builder(label).build()
        val icon = MonochromaticImage.Builder(Icon.createWithResource(this, R.drawable.ic_tile_qr)).build()
        val openCard = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java)
                .putExtra(Shortcuts.EXTRA, Shortcuts.QR)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        return when (type) {
            ComplicationType.SHORT_TEXT ->
                ShortTextComplicationData.Builder(PlainComplicationText.Builder("QR").build(), description)
                    .setMonochromaticImage(icon)
                    .setTapAction(openCard)
                    .build()
            ComplicationType.MONOCHROMATIC_IMAGE ->
                MonochromaticImageComplicationData.Builder(icon, description)
                    .setTapAction(openCard)
                    .build()
            else -> null
        }
    }
}
