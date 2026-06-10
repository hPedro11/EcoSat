package br.com.gs.ecosat.di

import br.com.gs.ecosat.domain.usecase.ClassifyRiskUseCase
import br.com.gs.ecosat.domain.usecase.GetAlertEventsUseCase
import br.com.gs.ecosat.domain.usecase.GetEventByIdUseCase
import br.com.gs.ecosat.domain.usecase.GetEventsUseCase
import br.com.gs.ecosat.domain.usecase.GetFavoriteEventsUseCase
import br.com.gs.ecosat.domain.usecase.IsOnboardingDoneUseCase
import br.com.gs.ecosat.domain.usecase.SetOnboardingDoneUseCase
import br.com.gs.ecosat.domain.usecase.ToggleFavoriteUseCase
import org.koin.dsl.module

val domainModule = module {

    // ClassifyRisk é injetado no Repository, então mantemos como single
    single { ClassifyRiskUseCase() }

    factory { GetEventsUseCase(repository = get()) }
    factory { GetEventByIdUseCase(repository = get()) }
    factory { GetAlertEventsUseCase(repository = get()) }
    factory { GetFavoriteEventsUseCase(eventRepository = get(), preferencesRepository = get()) }
    factory { ToggleFavoriteUseCase(repository = get()) }
    factory { IsOnboardingDoneUseCase(repository = get()) }
    factory { SetOnboardingDoneUseCase(repository = get()) }
}