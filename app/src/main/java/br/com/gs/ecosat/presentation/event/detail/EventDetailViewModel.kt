package br.com.gs.ecosat.presentation.event.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.repository.PreferencesRepository
import br.com.gs.ecosat.domain.usecase.GetEventByIdUseCase
import br.com.gs.ecosat.domain.usecase.ToggleFavoriteUseCase
import br.com.gs.ecosat.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventDetailUiData(
    val event: Event,
    val isFavorite: Boolean
)

class EventDetailViewModel(
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<EventDetailUiData>>(UiState.Initial)
    val uiState: StateFlow<UiState<EventDetailUiData>> = _uiState

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getEventByIdUseCase(eventId)) {
                is Resource.Success -> {
                    _uiState.value = UiState.Success(
                        EventDetailUiData(
                            event = result.data,
                            isFavorite = preferencesRepository.isFavorite(result.data.id)
                        )
                    )
                }
                is Resource.Error -> {
                    _uiState.value = UiState.Error(result.message)
                }
                Resource.Loading -> {
                    _uiState.value = UiState.Loading
                }
            }
        }
    }

    fun toggleFavorite() {
        val current = _uiState.value
        if (current is UiState.Success) {
            toggleFavoriteUseCase(current.data.event.id)
            _uiState.update {
                UiState.Success(
                    current.data.copy(
                        isFavorite = preferencesRepository.isFavorite(current.data.event.id)
                    )
                )
            }
        }
    }
}