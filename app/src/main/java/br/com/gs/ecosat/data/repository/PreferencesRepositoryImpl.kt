package br.com.gs.ecosat.data.repository

import br.com.gs.ecosat.data.local.PreferencesDataSource
import br.com.gs.ecosat.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(
    private val dataSource: PreferencesDataSource
) : PreferencesRepository {

    override fun isOnboardingDone(): Boolean = dataSource.isOnboardingDone()
    override fun setOnboardingDone() = dataSource.setOnboardingDone()
    override fun getFavorites(): Set<String> = dataSource.getFavorites()
    override fun isFavorite(eventId: String): Boolean = dataSource.isFavorite(eventId)
    override fun toggleFavorite(eventId: String) = dataSource.toggleFavorite(eventId)
}