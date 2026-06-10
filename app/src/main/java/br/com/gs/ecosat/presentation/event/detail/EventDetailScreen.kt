package br.com.gs.ecosat.presentation.event.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.presentation.common.UiState
import br.com.gs.ecosat.presentation.common.components.ErrorState
import br.com.gs.ecosat.presentation.common.components.LoadingState
import br.com.gs.ecosat.presentation.common.components.RiskBadge
import br.com.gs.ecosat.presentation.common.components.colorForRisk
import br.com.gs.ecosat.presentation.common.components.iconForCategory
import br.com.gs.ecosat.presentation.theme.EcoBlue
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    onBackClick: () -> Unit,
    viewModel: EventDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDispatchDialog by remember { mutableStateOf(false) }

    LaunchedEffect(eventId) { viewModel.loadEvent(eventId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do evento") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    val current = uiState
                    if (current is UiState.Success) {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (current.data.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favoritar"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (val state = uiState) {
                UiState.Initial, UiState.Loading -> LoadingState()
                is UiState.Error -> ErrorState(message = state.message, onRetry = { viewModel.loadEvent(eventId) })
                is UiState.Success -> {
                    DetailContent(
                        event = state.data.event,
                        onDispatchClick = { showDispatchDialog = true }
                    )
                }
            }
        }
    }

    if (showDispatchDialog) {
        val event = (uiState as? UiState.Success)?.data?.event
        AlertDialog(
            onDismissRequest = { showDispatchDialog = false },
            icon = { Icon(Icons.Filled.Groups, contentDescription = null, tint = EcoBlue) },
            title = { Text("Acionar equipe?") },
            text = {
                Text(
                    "Você confirma o acionamento de uma equipe de Defesa Civil para o evento " +
                            "\"${event?.title ?: ""}\"? Esta ação notificaria a central operacional."
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDispatchDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EcoBlue)
                ) { Text("Confirmar acionamento") }
            },
            dismissButton = {
                TextButton(onClick = { showDispatchDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun DetailContent(event: Event, onDispatchClick: () -> Unit) {
    val riskColor = colorForRisk(event.riskLevel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Banner com ícone grande do tipo de evento
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(riskColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconForCategory(event.categoryId),
                contentDescription = event.category,
                tint = riskColor,
                modifier = Modifier.size(80.dp)
            )
            RiskBadge(
                level = event.riskLevel,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
            )
            Text(
                text = "${event.mission} • ${event.date}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Título + localização
            Column {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    val local = if (event.latitude != null && event.longitude != null)
                        "  %.2f, %.2f".format(event.latitude, event.longitude)
                    else "  ${event.category}"
                    Text(text = local, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = event.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Seção Severidade
            DetailSection(
                icon = Icons.Filled.BarChart,
                iconColor = riskColor,
                title = "Severidade",
                content = "Nível ${event.severity}/10 — classificação ${event.riskLevel.label}. ${event.description}"
            )

            // Seção Impacto potencial
            DetailSection(
                icon = Icons.Filled.Park,
                iconColor = EcoBlue,
                title = "Impacto potencial",
                content = "Impacto estimado de %.1f/10. Risco para fauna, flora e comunidades locais na região monitorada.".format(event.impact)
            )

            // Seção Fonte dos dados
            DetailSection(
                icon = Icons.Filled.SatelliteAlt,
                iconColor = MaterialTheme.colorScheme.secondary,
                title = "Fonte dos dados",
                content = "Fonte: ${event.source}\nMissão: ${event.mission}\nTecnologia: ${event.technology}\nIndicador de alerta: ${if (event.isAlert) "SIM" else "NÃO"}"
            )

            // Botão Acionar equipe
            Button(
                onClick = onDispatchClick,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EcoBlue)
            ) {
                Icon(Icons.Filled.Groups, contentDescription = null)
                Text("  Acionar equipe", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DetailSection(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    content: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.size(12.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }
    }
}