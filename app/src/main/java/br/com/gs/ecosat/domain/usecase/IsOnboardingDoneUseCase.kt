package br.com.gs.ecosat.domain.usecase

import br.com.gs.ecosat.domain.repository.PreferencesRepository

class IsOnboardingDoneUseCase(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): Boolean = repository.isOnboardingDone()
}