package br.com.gs.ecosat.presentation.event.splash

import androidx.lifecycle.ViewModel
import br.com.gs.ecosat.domain.usecase.IsOnboardingDoneUseCase

class SplashViewModel(
    private val isOnboardingDoneUseCase: IsOnboardingDoneUseCase
) : ViewModel() {

    fun isOnboardingDone(): Boolean = isOnboardingDoneUseCase()
}