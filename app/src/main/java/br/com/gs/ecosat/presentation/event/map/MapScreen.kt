package br.com.gs.ecosat.presentation.event.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.gs.ecosat.presentation.common.UiState
import br.com.gs.ecosat.presentation.common.components.ErrorState
import br.com.gs.ecosat.presentation.common.components.LoadingState
import br.com.gs.ecosat.presentation.common.components.colorForRisk
import br.com.gs.ecosat.presentation.common.components.iconForCategory
import br.com.gs.ecosat.presentation.event.list.EventListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    onEventClick: (String) -> Unit,
    viewModel: EventListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadEvents() }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            UiState.Initial, UiState.Loading -> LoadingState()
            is UiState.Error -> ErrorState(message = state.message, onRetry = { viewModel.loadEvents() })
            is UiState.Success -> {
                val located = state.data.all.filter { it.latitude != null && it.longitude != null }
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Map, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Text(
                                    text = "  Localização dos eventos",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "${located.size} eventos georreferenciados",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(items = located, key = { it.id }) { event ->
                                    Card(
                                        onClick = { onEventClick(event.id) },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        )
                                    ) {
                                        androidx.compose.foundation.layout.Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Place,
                                                contentDescription = null,
                                                tint = colorForRisk(event.riskLevel),
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                                Text(event.title, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
                                                Text(
                                                    "%.3f, %.3f".format(event.latitude, event.longitude),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}