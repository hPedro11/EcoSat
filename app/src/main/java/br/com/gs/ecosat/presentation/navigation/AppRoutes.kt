package br.com.gs.ecosat.presentation.navigation

object AppRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val MAIN = "main"
    const val EVENT_DETAIL = "event_detail/{eventId}"
    const val ALERTS = "alerts"
    const val FAVORITES = "favorites"

    fun eventDetail(eventId: String) = "event_detail/$eventId"
}