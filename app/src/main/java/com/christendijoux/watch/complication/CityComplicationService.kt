package com.christendijoux.watch.complication

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.util.CurrentCity

/**
 * Complication « Ville » : la ville où se trouve la montre, affichée en bas du cadran Christen Dijoux.
 * Rafraîchie toutes les 30 minutes (UPDATE_PERIOD_SECONDS) et à chaque ouverture de l'app.
 */
class CityComplicationService : SuspendingComplicationDataSourceService() {

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        if (request.complicationType != ComplicationType.SHORT_TEXT) return null
        val city = CurrentCity.refresh(this) ?: return NoDataComplicationData()
        return cityData(city)
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? =
        if (type == ComplicationType.SHORT_TEXT) cityData(Portfolio.CITY) else null

    private fun cityData(city: String): ComplicationData {
        val text = PlainComplicationText.Builder(city).build()
        return ShortTextComplicationData.Builder(text, text).build()
    }
}
