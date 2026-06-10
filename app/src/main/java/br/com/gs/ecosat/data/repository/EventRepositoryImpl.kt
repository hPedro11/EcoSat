package br.com.gs.ecosat.data.repository

import br.com.gs.ecosat.data.model.toDomain
import br.com.gs.ecosat.data.remote.EventRemoteDataSource
import br.com.gs.ecosat.domain.common.Resource
import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.repository.EventRepository
import br.com.gs.ecosat.domain.usecase.ClassifyRiskUseCase

class EventRepositoryImpl(
    private val remoteDataSource: EventRemoteDataSource,
    private val classifyRisk: ClassifyRiskUseCase
) : EventRepository {

    override suspend fun getEvents(): Resource<List<Event>> {
        return try {
            val response = remoteDataSource.getEvents()
            Resource.Success(response.events.map { it.toDomain(classifyRisk) })
        } catch (exception: Exception) {
            Resource.Error("Não foi possível carregar os eventos.")
        }
    }

    override suspend fun getEventById(id: String): Resource<Event> {
        return try {
            val response = remoteDataSource.getEventById(id)
            Resource.Success(response.toDomain(classifyRisk))
        } catch (exception: Exception) {
            Resource.Error("Não foi possível carregar o evento.")
        }
    }
}