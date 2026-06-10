package br.com.gs.ecosat.domain.usecase

import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.repository.EventRepository

class GetEventsUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(): Resource<List<Event>> {
        return repository.getEvents()
    }
}