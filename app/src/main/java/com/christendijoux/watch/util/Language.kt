package com.christendijoux.watch.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import androidx.wear.tiles.TileService
import com.christendijoux.watch.tile.ContactTileService
import java.util.Locale

/**
 * Langue de l'app, choisie dans le menu et indépendante de celle de la montre.
 * Tant que rien n'est choisi, l'app suit la langue du système (anglais si elle n'est pas prise en charge).
 */
object AppLanguage {
    /** Langues disponibles, dans l'ordre du sélecteur. */
    val supported = listOf("fr", "en")

    private const val PREFS = "settings"
    private const val KEY = "language"

    /** Langue affichée : celle choisie, sinon celle du système si elle est prise en charge, sinon l'anglais. */
    fun current(context: Context): String =
        saved(context) ?: systemLanguage(context).takeIf { it in supported } ?: "en"

    /**
     * Enregistre le choix, même s'il correspond à la langue affichée : il ne doit plus suivre la montre.
     * L'activité n'est recréée que si la langue change.
     */
    fun set(activity: Activity, language: String) {
        val changed = language != current(activity)
        prefs(activity).edit().putString(KEY, language).apply()
        if (changed) {
            // La tuile affiche aussi des textes : on demande au système de la redessiner
            TileService.getUpdater(activity).requestUpdate(ContactTileService::class.java)
            activity.recreate()
        }
    }

    /**
     * Contexte de base de l'activité, aux ressources de la langue affichée (à appeler dans attachBaseContext).
     * Une langue système non prise en charge est aussi remplacée, pour que la date suive les textes.
     */
    fun wrap(base: Context): Context {
        val language = current(base)
        // Même langue que le système : on garde sa région (fr-CA, en-GB…) pour les dates
        if (language == systemLanguage(base)) return base
        val config = Configuration(base.resources.configuration)
        config.setLocales(LocaleList(Locale.forLanguageTag(language)))
        return base.createConfigurationContext(config)
    }

    private fun systemLanguage(context: Context): String = context.resources.configuration.locales[0].language

    private fun saved(context: Context): String? =
        prefs(context).getString(KEY, null)?.takeIf { it in supported }

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
