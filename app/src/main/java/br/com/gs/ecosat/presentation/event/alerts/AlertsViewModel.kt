package br.com.gs.ecosat.presentation.event.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.usecase.GetAlertEventsUseCase
import br.com.gs.ecosat.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertsViewModel(
    private val getAlertEventsUseCase: GetAlertEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Event>>>(UiState.Initial)
    val uiState: StateFlow<UiState<List<Event>>> = _uiState

    fun loadAlerts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getAlertEventsUseCase()) {
                is Resource.Success -> _uiState.value = UiState.Success(result.data)
                is Resource.Error -> _uiState.value = UiState.Error(result.message)
                Resource.Loading -> _uiState.value = UiState.Loading
            }
        }
    }
}