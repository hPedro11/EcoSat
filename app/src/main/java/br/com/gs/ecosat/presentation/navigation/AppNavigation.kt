package br.com.gs.ecosat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.gs.ecosat.presentation.event.alerts.AlertsScreen
import br.com.gs.ecosat.presentation.event.detail.EventDetailScreen
import br.com.gs.ecosat.presentation.event.favorites.FavoritesScreen
import br.com.gs.ecosat.presentation.event.onboarding.OnboardingScreen
import br.com.gs.ecosat.presentation.event.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {

        composable(AppRoutes.SPLASH) {
            SplashScreen(
                onNavigateOnboarding = {
                    navController.navigate(AppRoutes.ONBOARDING) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateHome = {
                    navController.navigate(AppRoutes.MAIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.ONBOARDING) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(AppRoutes.MAIN) {
                        popUpTo(AppRoutes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        // Container com a bottom bar
        composable(AppRoutes.MAIN) {
            MainScreen(rootNavController = navController)
        }

        // Telas "de cima" (push) — mantêm TopAppBar própria com botão voltar
        composable(
            route = AppRoutes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventDetailScreen(
                eventId = eventId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ALERTS) {
            AlertsScreen(
                onEventClick = { id -> navController.navigate(AppRoutes.eventDetail(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.FAVORITES) {
            FavoritesScreen(
                onEventClick = { id -> navController.navigate(AppRoutes.eventDetail(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}