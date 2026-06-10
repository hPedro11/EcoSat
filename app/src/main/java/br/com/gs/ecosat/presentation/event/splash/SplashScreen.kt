package br.com.gs.ecosat.presentation.event.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    onNavigateOnboarding: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        delay(1800)
        if (viewModel.isOnboardingDone()) onNavigateHome() else onNavigateOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.SatelliteAlt,
                contentDescription = "EcoSat Logo",
                tint = Color.White,
                modifier = Modifier.size(96.dp).padding(16.dp)
            )
            Text(
                text = "EcoSat",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Monitoramento ambiental por satélite",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}