package br.com.gs.ecosat.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesDataSourceImpl(context: Context) : PreferencesDataSource {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ecosat_prefs", Context.MODE_PRIVATE)

    override fun isOnboardingDone(): Boolean = prefs.getBoolean(KEY_ONBOARDING, false)

    override fun setOnboardingDone() {
        prefs.edit().putBoolean(KEY_ONBOARDING, true).apply()
    }

    override fun getFavorites(): Set<String> =
        prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()

    override fun toggleFavorite(eventId: String) {
        val current = getFavorites().toMutableSet()
        if (current.contains(eventId)) current.remove(eventId) else current.add(eventId)
        prefs.edit().putStringSet(KEY_FAVORITES, current).apply()
    }

    override fun isFavorite(eventId: String): Boolean = getFavorites().contains(eventId)

    companion object {
        private const val KEY_ONBOARDING = "onboarding_done"
        private const val KEY_FAVORITES = "favorites"
    }
}