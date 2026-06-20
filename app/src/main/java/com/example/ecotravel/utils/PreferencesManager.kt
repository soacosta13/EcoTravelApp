package com.example.ecotravel.utils

import android.content.Context

/**
 * encapsula shared preferences (distancia total, ultimo destino, modo oscuro/claro)
 */
class PreferencesManager (context: Context) {

    companion object {
        private const val PREFS_NAME = "ecotravel_prefs"
        private const val KEY_TOTAL_DISTANCE = "total_distance"
        private const val KEY_LAST_DESTINATION = "last_destination"
        private const val KEY_DARK_MODE = "dark_mode"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    //distancia total
    fun getTotalDistance(): Double {
        return Double.fromBits(prefs.getLong(KEY_TOTAL_DISTANCE, 0L))
    }

    fun addToTotalDistance(km: Double) {
        val current = getTotalDistance()
        val newTotal = current + km
        prefs.edit().putLong(KEY_TOTAL_DISTANCE, newTotal.toBits()).apply()
    }

    //ultimo destino
    fun getLastDestination(): String {
        return prefs.getString(KEY_LAST_DESTINATION, "") ?: ""
    }

    fun saveLastDestination(destination: String) {
        prefs.edit().putString(KEY_LAST_DESTINATION, destination)
    }

    //dark mode
    fun isDarkModeEnabled(): Boolean {
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}