package com.christendijoux.watch.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
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

    /** Langue affichée : celle choisie, sinon celle du système si elle est prise en charge. */
    fun current(context: Context): String =
        saved(context) ?: context.resources.configuration.locales[0].language.takeIf { it in supported } ?: "en"

    /** Enregistre le choix puis recrée l'activité pour appliquer la langue à tout l'écran. */
    fun set(activity: Activity, language: String) {
        if (language == current(activity)) return
        prefs(activity).edit().putString(KEY, language).apply()
        activity.recreate()
    }

    /** Contexte de base de l'activité, aux ressources de la langue choisie (à appeler dans attachBaseContext). */
    fun wrap(base: Context): Context {
        val language = saved(base) ?: return base
        val config = Configuration(base.resources.configuration)
        config.setLocales(LocaleList(Locale.forLanguageTag(language)))
        return base.createConfigurationContext(config)
    }

    private fun saved(context: Context): String? =
        prefs(context).getString(KEY, null)?.takeIf { it in supported }

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
