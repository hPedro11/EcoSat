package br.com.gs.ecosat.data.remote

import br.com.gs.ecosat.data.model.EventListResponse
import br.com.gs.ecosat.data.model.EventResponse

interface EventRemoteDataSource {
    suspend fun getEvents(): EventListResponse
    suspend fun getEventById(id: String): EventResponse
}