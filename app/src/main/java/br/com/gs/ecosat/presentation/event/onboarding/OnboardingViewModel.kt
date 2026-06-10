package br.com.gs.ecosat.presentation.event.onboarding

import androidx.lifecycle.ViewModel
import br.com.gs.ecosat.domain.usecase.SetOnboardingDoneUseCase

class OnboardingViewModel(
    private val setOnboardingDoneUseCase: SetOnboardingDoneUseCase
) : ViewModel() {

    fun finishOnboarding() {
        setOnboardingDoneUseCase()
    }
}