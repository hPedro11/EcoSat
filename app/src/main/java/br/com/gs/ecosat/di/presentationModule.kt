package br.com.gs.ecosat.di

import br.com.gs.ecosat.presentation.event.alerts.AlertsViewModel
import br.com.gs.ecosat.presentation.event.detail.EventDetailViewModel
import br.com.gs.ecosat.presentation.event.favorites.FavoritesViewModel
import br.com.gs.ecosat.presentation.event.home.HomeViewModel
import br.com.gs.ecosat.presentation.event.list.EventListViewModel
import br.com.gs.ecosat.presentation.event.onboarding.OnboardingViewModel
import br.com.gs.ecosat.presentation.event.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::EventListViewModel)
    viewModelOf(::EventDetailViewModel)
    viewModelOf(::AlertsViewModel)
    viewModelOf(::FavoritesViewModel)
}