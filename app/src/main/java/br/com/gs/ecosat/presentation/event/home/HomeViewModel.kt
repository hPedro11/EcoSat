package br.com.gs.ecosat.presentation.event.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.model.RiskLevel
import br.com.gs.ecosat.domain.usecase.GetEventsUseCase
import br.com.gs.ecosat.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

data class HomeUiData(
    val total: Int,
    val critico: Int,
    val alto: Int,
    val medio: Int,
    val baixo: Int,
    val recent: List<Event>,
    // série de 7 pontos para cada classe de risco (gráfico de tendência)
    val trendCritico: List<Float>,
    val trendAlto: List<Float>,
    val trendMedio: List<Float>,
    val trendBaixo: List<Float>
)

class HomeViewModel(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeUiData>>(UiState.Initial)
    val uiState: StateFlow<UiState<HomeUiData>> = _uiState

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getEventsUseCase()) {
                is Resource.Success -> {
                    val events = result.data
                    val critico = events.count { it.riskLevel == RiskLevel.CRITICO }
                    val alto = events.count { it.riskLevel == RiskLevel.ALTO }
                    val medio = events.count { it.riskLevel == RiskLevel.MEDIO }
                    val baixo = events.count { it.riskLevel == RiskLevel.BAIXO }

                    _uiState.value = UiState.Success(
                        HomeUiData(
                            total = events.size,
                            critico = critico,
                            alto = alto,
                            medio = medio,
                            baixo = baixo,
                            recent = events.take(5),
                            trendCritico = buildTrend(critico),
                            trendAlto = buildTrend(alto),
                            trendMedio = buildTrend(medio),
                            trendBaixo = buildTrend(baixo)
                        )
                    )
                }
                is Resource.Error -> _uiState.value = UiState.Error(result.message)
                Resource.Loading -> _uiState.value = UiState.Loading
            }
        }
    }

    /**
     * Gera 7 pontos de tendência terminando no valor atual.
     * Determinístico (sem random) para o gráfico não "pular" a cada recomposição.
     */
    private fun buildTrend(current: Int): List<Float> {
        val base = current.coerceAtLeast(1)
        return (0..6).map { day ->
            val wave = (abs((day * 37) % 5) - 2)  // varia entre -2 e +2 de forma fixa
            (base + wave - (6 - day)).coerceAtLeast(0).toFloat()
        }.let { list ->
            // garante que o último ponto seja o valor atual
            list.dropLast(1) + current.toFloat()
        }
    }
}