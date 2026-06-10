package br.com.gs.ecosat.domain.usecase

import br.com.gs.ecosat.domain.repository.PreferencesRepository

class ToggleFavoriteUseCase(
    private val repository: PreferencesRepository
) {
    operator fun invoke(eventId: String) {
        repository.toggleFavorite(eventId)
    }
}