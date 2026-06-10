package br.com.gs.ecosat.domain.usecase

import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.repository.EventRepository
import br.com.gs.ecosat.domain.repository.PreferencesRepository

class GetFavoriteEventsUseCase(
    private val eventRepository: EventRepository,
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Resource<List<Event>> {
        val favorites = preferencesRepository.getFavorites()
        if (favorites.isEmpty()) {
            return Resource.Success(emptyList())
        }
        return when (val result = eventRepository.getEvents()) {
            is Resource.Success -> Resource.Success(result.data.filter { favorites.contains(it.id) })
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
        }
    }
}