package br.com.gs.ecosat.domain.repository

import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event

interface EventRepository {
    suspend fun getEvents(): Resource<List<Event>>
    suspend fun getEventById(id: String): Resource<Event>
}