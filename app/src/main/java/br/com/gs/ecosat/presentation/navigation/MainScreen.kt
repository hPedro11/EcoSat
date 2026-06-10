package br.com.gs.ecosat.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import br.com.gs.ecosat.presentation.event.home.HomeScreen
import br.com.gs.ecosat.presentation.event.list.EventListScreen
import br.com.gs.ecosat.presentation.event.map.MapScreen
import br.com.gs.ecosat.presentation.event.reports.ReportsScreen
import br.com.gs.ecosat.presentation.event.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(rootNavController: NavHostController) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val currentTab = BottomNavItem.items.firstOrNull { it.route == currentRoute }
        ?: BottomNavItem.Dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (currentTab == BottomNavItem.Dashboard) "EcoSat" else currentTab.label,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                shadowElevation = 8.dp
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    BottomNavItem.items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                tabNavController.navigate(item.route) {
                                    popUpTo(tabNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                indicatorColor = Color.White.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = tabNavController,
            startDestination = BottomNavItem.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Dashboard.route) {
                HomeScreen(
                    onEventClick = { id -> rootNavController.navigate(AppRoutes.eventDetail(id)) },
                    onOpenEvents = { tabNavController.navigate(BottomNavItem.Events.route) },
                    onOpenAlerts = { rootNavController.navigate(AppRoutes.ALERTS) },
                    onOpenFavorites = { rootNavController.navigate(AppRoutes.FAVORITES) }
                )
            }
            composable(BottomNavItem.Map.route) {
                MapScreen(
                    onEventClick = { id -> rootNavController.navigate(AppRoutes.eventDetail(id)) }
                )
            }
            composable(BottomNavItem.Events.route) {
                EventListScreen(
                    onEventClick = { id -> rootNavController.navigate(AppRoutes.eventDetail(id)) },
                    onBackClick = { tabNavController.navigate(BottomNavItem.Dashboard.route) }
                )
            }
            composable(BottomNavItem.Reports.route) {
                ReportsScreen()
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen(
                    onOpenFavorites = { rootNavController.navigate(AppRoutes.FAVORITES) },
                    onOpenAlerts = { rootNavController.navigate(AppRoutes.ALERTS) }
                )
            }
        }
    }
}