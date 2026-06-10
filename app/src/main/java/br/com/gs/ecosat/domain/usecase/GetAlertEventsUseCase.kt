package br.com.gs.ecosat.domain.usecase

import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.repository.EventRepository

class GetAlertEventsUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(): Resource<List<Event>> {
        return when (val result = repository.getEvents()) {
            is Resource.Success -> Resource.Success(result.data.filter { it.isAlert })
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
        }
    }
}