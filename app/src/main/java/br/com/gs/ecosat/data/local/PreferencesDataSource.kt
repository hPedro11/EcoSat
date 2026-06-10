package br.com.gs.ecosat.data.local

interface PreferencesDataSource {
    fun isOnboardingDone(): Boolean
    fun setOnboardingDone()
    fun getFavorites(): Set<String>
    fun toggleFavorite(eventId: String)
    fun isFavorite(eventId: String): Boolean
}