package br.com.gs.ecosat.domain.repository

interface PreferencesRepository {
    fun isOnboardingDone(): Boolean
    fun setOnboardingDone()
    fun getFavorites(): Set<String>
    fun isFavorite(eventId: String): Boolean
    fun toggleFavorite(eventId: String)
}