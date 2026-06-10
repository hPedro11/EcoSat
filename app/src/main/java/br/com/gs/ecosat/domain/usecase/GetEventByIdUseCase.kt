package br.com.gs.ecosat.domain.usecase

import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.repository.EventRepository

class GetEventByIdUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: String): Resource<Event> {
        if (id.isBlank()) {
            return Resource.Error("Identificador do evento inválido.")
        }
        return repository.getEventById(id)
    }
}