package br.com.gs.ecosat.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Dashboard : BottomNavItem("tab_dashboard", "Dashboard", Icons.Filled.Dashboard)
    data object Map : BottomNavItem("tab_map", "Mapa", Icons.Filled.Map)
    data object Events : BottomNavItem("tab_events", "Eventos", Icons.Filled.Notifications)
    data object Reports : BottomNavItem("tab_reports", "Relatórios", Icons.Filled.Assessment)
    data object Settings : BottomNavItem("tab_settings", "Configurações", Icons.Filled.Settings)

    companion object {
        val items = listOf(Dashboard, Map, Events, Reports, Settings)
    }
}