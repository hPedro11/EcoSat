package br.com.gs.ecosat.data.remote

import br.com.gs.ecosat.data.model.EventListResponse
import br.com.gs.ecosat.data.model.EventResponse

class EventRemoteDataSourceImpl(
    private val api: EonetApi
) : EventRemoteDataSource {

    override suspend fun getEvents(): EventListResponse {
        return api.getEvents()
    }

    override suspend fun getEventById(id: String): EventResponse {
        return api.getEventById(id)
    }
}