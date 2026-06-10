package br.com.gs.ecosat.presentation.event.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.gs.ecosat.presentation.common.UiState
import br.com.gs.ecosat.presentation.common.components.ErrorState
import br.com.gs.ecosat.presentation.common.components.EventCard
import br.com.gs.ecosat.presentation.common.components.LoadingState
import br.com.gs.ecosat.presentation.theme.RiskCritical
import br.com.gs.ecosat.presentation.theme.RiskHigh
import br.com.gs.ecosat.presentation.theme.RiskLow
import br.com.gs.ecosat.presentation.theme.RiskMedium
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onOpenEvents: () -> Unit,
    onOpenAlerts: () -> Unit,
    onOpenFavorites: () -> Unit,
    onEventClick: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadHome() }

    when (val state = uiState) {
        UiState.Initial, UiState.Loading -> LoadingState()
        is UiState.Error -> ErrorState(message = state.message, onRetry = { viewModel.loadHome() })
        is UiState.Success -> HomeContent(
            data = state.data,
            onOpenEvents = onOpenEvents,
            onEventClick = onEventClick
        )
    }
}

@Composable
private fun HomeContent(
    data: HomeUiData,
    onOpenEvents: () -> Unit,
    onEventClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Cabeçalho: saudação + localização
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Olá!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Visão geral",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Situação ambiental atual",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "  São Paulo – SP",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Resumo de riscos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Grade 2x2 de cards de risco
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                RiskCard("CRÍTICO", data.critico, Icons.Filled.LocalFireDepartment, RiskCritical, Modifier.weight(1f))
                RiskCard("ALTO", data.alto, Icons.Filled.NotificationsActive, RiskHigh, Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                RiskCard("MÉDIO", data.medio, Icons.Filled.WbSunny, RiskMedium, Modifier.weight(1f))
                RiskCard("BAIXO", data.baixo, Icons.Filled.Park, RiskLow, Modifier.weight(1f))
            }
        }

        // Gráfico de tendência
        item {
            TrendCard(data)
        }

        // Lista de eventos recentes
        item {
            Text(
                text = "Eventos recentes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        items(items = data.recent, key = { it.id }) { event ->
            EventCard(
                event = event,
                isFavorite = false,
                onClick = { onEventClick(event.id) },
                onToggleFavorite = { }
            )
        }
        item {
            OutlinedButton(
                onClick = onOpenEvents,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver lista completa de eventos")
            }
        }
    }
}

@Composable
private fun RiskCard(
    label: String,
    count: Int,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Text(text = "$count", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = "Eventos", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun TrendCard(data: HomeUiData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tendência (7 dias)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            val series = listOf(
                data.trendCritico to RiskCritical,
                data.trendAlto to RiskHigh,
                data.trendMedio to RiskMedium,
                data.trendBaixo to RiskLow
            )

            val maxValue = series.flatMap { it.first }.maxOrNull()?.coerceAtLeast(1f) ?: 1f

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(top = 12.dp)
            ) {
                val w = size.width
                val h = size.height

                series.forEach { (points, color) ->
                    if (points.size < 2) return@forEach
                    val stepX = w / (points.size - 1)

                    val offsets = points.mapIndexed { index, value ->
                        val x = stepX * index
                        val y = h - (value / maxValue) * h
                        Offset(x, y)
                    }

                    for (i in 0 until offsets.size - 1) {
                        drawLine(
                            color = color,
                            start = offsets[i],
                            end = offsets[i + 1],
                            strokeWidth = 6f,
                            cap = StrokeCap.Round
                        )
                    }
                    offsets.forEach { p ->
                        drawCircle(color = color, radius = 7f, center = p)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LegendDot("Crítico", RiskCritical)
                LegendDot("Alto", RiskHigh)
                LegendDot("Médio", RiskMedium)
                LegendDot("Baixo", RiskLow)
            }
        }
    }
}

@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
        Text(
            text = " $label",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}