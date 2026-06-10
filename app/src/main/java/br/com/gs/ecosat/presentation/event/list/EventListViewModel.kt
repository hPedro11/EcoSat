package br.com.gs.ecosat.presentation.event.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.model.RiskLevel
import br.com.gs.ecosat.domain.repository.PreferencesRepository
import br.com.gs.ecosat.domain.usecase.GetEventsUseCase
import br.com.gs.ecosat.domain.usecase.ToggleFavoriteUseCase
import br.com.gs.ecosat.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SortOrder(val label: String) {
    RISK("Maior risco"),
    DATE("Mais recentes")
}

data class EventListUiData(
    val all: List<Event>,
    val riskFilter: RiskLevel? = null,
    val sortOrder: SortOrder = SortOrder.RISK,
    val favorites: Set<String> = emptySet()
) {
    val filtered: List<Event>
        get() {
            val byRisk = if (riskFilter == null) all else all.filter { it.riskLevel == riskFilter }
            return when (sortOrder) {
                SortOrder.RISK -> byRisk.sortedWith(
                    compareBy({ it.riskLevel.ordinal }, { -it.severity }, { -it.impact })
                )
                SortOrder.DATE -> byRisk.sortedByDescending { it.date }
            }
        }
}

class EventListViewModel(
    private val getEventsUseCase: GetEventsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<EventListUiData>>(UiState.Initial)
    val uiState: StateFlow<UiState<EventListUiData>> = _uiState

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getEventsUseCase()) {
                is Resource.Success -> {
                    _uiState.value = UiState.Success(
                        EventListUiData(
                            all = result.data,
                            favorites = preferencesRepository.getFavorites()
                        )
                    )
                }
                is Resource.Error -> _uiState.value = UiState.Error(result.message)
                Resource.Loading -> _uiState.value = UiState.Loading
            }
        }
    }

    fun setRiskFilter(level: RiskLevel?) {
        _uiState.update { current ->
            if (current is UiState.Success) UiState.Success(current.data.copy(riskFilter = level))
            else current
        }
    }

    fun setSortOrder(order: SortOrder) {
        _uiState.update { current ->
            if (current is UiState.Success) UiState.Success(current.data.copy(sortOrder = order))
            else current
        }
    }

    fun toggleFavorite(eventId: String) {
        toggleFavoriteUseCase(eventId)
        _uiState.update { current ->
            if (current is UiState.Success) {
                UiState.Success(current.data.copy(favorites = preferencesRepository.getFavorites()))
            } else current
        }
    }
}