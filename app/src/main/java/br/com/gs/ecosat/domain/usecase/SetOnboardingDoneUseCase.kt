package br.com.gs.ecosat.domain.usecase

import br.com.gs.ecosat.domain.repository.PreferencesRepository

class SetOnboardingDoneUseCase(
    private val repository: PreferencesRepository
) {
    operator fun invoke() = repository.setOnboardingDone()
}