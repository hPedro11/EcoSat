package br.com.gs.ecosat.data.remote

import br.com.gs.ecosat.data.model.EventListResponse
import br.com.gs.ecosat.data.model.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EonetApi {

    @GET("api/v3/events")
    suspend fun getEvents(
        @Query("limit") limit: Int = 50,
        @Query("days") days: Int = 30,
        @Query("status") status: String = "open"
    ): EventListResponse

    @GET("api/v3/events/{id}")
    suspend fun getEventById(
        @Path("id") id: String
    ): EventResponse
}