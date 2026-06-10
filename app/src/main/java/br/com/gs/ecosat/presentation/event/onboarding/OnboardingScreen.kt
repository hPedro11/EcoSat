package br.com.gs.ecosat.presentation.event.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

private data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val description: String
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val pages = remember {
        listOf(
            OnboardingPage(
                icon = Icons.Filled.SatelliteAlt,
                title = "Dados de satélite",
                description = "Consumimos dados públicos da NASA EONET para monitorar " +
                        "eventos ambientais em tempo quase real."
            ),
            OnboardingPage(
                icon = Icons.Filled.LocalFireDepartment,
                title = "Queimadas, secas e tempestades",
                description = "Centralizamos eventos críticos em uma única interface, " +
                        "classificados por severidade e impacto estimado."
            ),
            OnboardingPage(
                icon = Icons.Filled.Notifications,
                title = "Alertas para Defesa Civil",
                description = "Receba alertas de eventos ALTO e CRÍTICO e tome decisões " +
                        "mais rápidas em campo ou na central de comando."
            )
        )
    }

    var index by remember { mutableIntStateOf(0) }
    val current = pages[index]

    Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = current.icon,
                contentDescription = current.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = current.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = current.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { if (index > 0) index-- },
                enabled = index > 0
            ) { Text("Voltar") }

            Button(onClick = {
                if (index < pages.lastIndex) {
                    index++
                } else {
                    viewModel.finishOnboarding()
                    onFinish()
                }
            }) {
                Text(if (index == pages.lastIndex) "Começar" else "Avançar")
            }
        }

        Text(
            text = "${index + 1} / ${pages.size}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}