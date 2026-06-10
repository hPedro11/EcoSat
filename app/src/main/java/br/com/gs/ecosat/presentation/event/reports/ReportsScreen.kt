package br.com.gs.ecosat.presentation.event.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.gs.ecosat.presentation.common.UiState
import br.com.gs.ecosat.presentation.common.components.ErrorState
import br.com.gs.ecosat.presentation.common.components.LoadingState
import br.com.gs.ecosat.presentation.common.components.colorForRisk
import br.com.gs.ecosat.presentation.event.list.EventListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportsScreen(viewModel: EventListViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadEvents() }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            UiState.Initial, UiState.Loading -> LoadingState()
            is UiState.Error -> ErrorState(message = state.message, onRetry = { viewModel.loadEvents() })
            is UiState.Success -> {
                val events = state.data.all
                val byCategory = events.groupingBy { it.category }.eachCount()
                    .entries.sortedByDescending { it.value }
                val maxCat = byCategory.maxOfOrNull { it.value }?.coerceAtLeast(1) ?: 1

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Relatório analítico",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Total de ${events.size} eventos monitorados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Eventos por tipo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            byCategory.forEach { (category, count) ->
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(category, style = MaterialTheme.typography.bodyMedium)
                                        Text("$count", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    }
                                    // Barra de progresso simples
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MaterialTheme.colorScheme.background)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(count.toFloat() / maxCat)
                                                .height(8.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Eventos por classe de risco", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            br.com.gs.ecosat.domain.model.RiskLevel.values().forEach { level ->
                                val count = events.count { it.riskLevel == level }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .height(12.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(colorForRisk(level))
                                                .padding(6.dp)
                                        ) {}
                                        Text("  ${level.label}", style = MaterialTheme.typography.bodyMedium)
                                    }
                                    Text("$count eventos", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}