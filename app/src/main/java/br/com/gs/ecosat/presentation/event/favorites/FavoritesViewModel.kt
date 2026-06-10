package br.com.gs.ecosat.presentation.event.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.usecase.GetFavoriteEventsUseCase
import br.com.gs.ecosat.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteEventsUseCase: GetFavoriteEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Event>>>(UiState.Initial)
    val uiState: StateFlow<UiState<List<Event>>> = _uiState

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getFavoriteEventsUseCase()) {
                is Resource.Success -> _uiState.value = UiState.Success(result.data)
                is Resource.Error -> _uiState.value = UiState.Error(result.message)
                Resource.Loading -> _uiState.value = UiState.Loading
            }
        }
    }
}