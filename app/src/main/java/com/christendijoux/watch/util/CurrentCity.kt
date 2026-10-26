package com.christendijoux.watch.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import com.christendijoux.watch.complication.CityComplicationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * Ville actuelle, affichée en bas du cadran : position approximative de la montre, puis nom de la ville.
 * La dernière ville trouvée est gardée pour les moments où la position n'est pas disponible
 * (app en arrière-plan, pas de réseau).
 */
object CurrentCity {
    private const val PREFS = "city"
    private const val KEY = "name"
    private const val LOCATION_TIMEOUT_MS = 8_000L

    fun hasPermission(context: Context): Boolean =
        context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    fun cached(context: Context): String? = prefs(context).getString(KEY, null)

    /** Cherche la ville actuelle ; renvoie la dernière connue si la position ou le nom sont introuvables. */
    suspend fun refresh(context: Context): String? {
        if (!hasPermission(context)) return cached(context)
        val location = location(context) ?: return cached(context)
        val city = cityName(context, location) ?: return cached(context)
        prefs(context).edit().putString(KEY, city).apply()
        return city
    }

    /** Demande au cadran de redessiner la ville (après une nouvelle position ou l'autorisation). */
    fun requestComplicationUpdate(context: Context) {
        ComplicationDataSourceUpdateRequester
            .create(context, ComponentName(context, CityComplicationService::class.java))
            .requestUpdateAll()
    }

    // L'autorisation est vérifiée dans refresh() avant tout appel
    @SuppressLint("MissingPermission")
    private suspend fun location(context: Context): Location? {
        val manager = context.getSystemService(LocationManager::class.java) ?: return null
        val providers = buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) add(LocationManager.FUSED_PROVIDER)
            add(LocationManager.NETWORK_PROVIDER)
            add(LocationManager.GPS_PROVIDER)
        }.filter { runCatching { manager.isProviderEnabled(it) }.getOrDefault(false) }

        for (provider in providers) {
            val current = withTimeoutOrNull(LOCATION_TIMEOUT_MS) {
                suspendCancellableCoroutine { continuation ->
                    val signal = CancellationSignal()
                    continuation.invokeOnCancellation { signal.cancel() }
                    manager.getCurrentLocation(provider, signal, context.mainExecutor) { continuation.resume(it) }
                }
            }
            if (current != null) return current
        }
        // Repli : dernière position connue du système, même un peu ancienne
        return providers.firstNotNullOfOrNull { runCatching { manager.getLastKnownLocation(it) }.getOrNull() }
    }

    private suspend fun cityName(context: Context, location: Location): String? {
        if (!Geocoder.isPresent()) return null
        val geocoder = Geocoder(context)
        val address: Address? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            withTimeoutOrNull(LOCATION_TIMEOUT_MS) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1,
                        object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                continuation.resume(addresses.firstOrNull())
                            }

                            override fun onError(errorMessage: String?) {
                                continuation.resume(null)
                            }
                        },
                    )
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                @Suppress("DEPRECATION")
                runCatching { geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull() }.getOrNull()
            }
        }
        return address?.let { it.locality ?: it.subAdminArea ?: it.adminArea }?.takeIf { it.isNotBlank() }
    }

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
