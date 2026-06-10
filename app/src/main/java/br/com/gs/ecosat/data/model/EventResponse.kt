package br.com.gs.ecosat.data.model

import br.com.gs.ecosat.domain.model.Event
import br.com.gs.ecosat.domain.model.RiskLevel
import br.com.gs.ecosat.domain.usecase.ClassifyRiskUseCase
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlin.math.min

@Serializable
data class EventListResponse(
    val title: String? = null,
    val description: String? = null,
    val events: List<EventResponse> = emptyList()
)

@Serializable
data class EventResponse(
    val id: String,
    val title: String,
    val description: String? = null,
    val link: String? = null,
    val closed: String? = null,
    val categories: List<CategoryResponse> = emptyList(),
    val sources: List<SourceResponse> = emptyList(),
    val geometry: List<GeometryResponse> = emptyList()
)

@Serializable
data class CategoryResponse(
    val id: String? = null,
    val title: String? = null
)

@Serializable
data class SourceResponse(
    val id: String? = null,
    val url: String? = null
)

@Serializable
data class GeometryResponse(
    val magnitudeValue: Double? = null,
    val magnitudeUnit: String? = null,
    val date: String? = null,
    val type: String? = null,
    val coordinates: JsonElement? = null
)

/**
 * Converte EventResponse (API) em Event (domínio),
 * aplicando o motor de classificação de risco.
 */
fun EventResponse.toDomain(classifyRisk: ClassifyRiskUseCase): Event {
    val categoryId = categories.firstOrNull()?.id.orEmpty()
    val categoryTitle = categories.firstOrNull()?.title ?: "Desconhecido"

    val severity = baseSeverityFor(categoryId)
    val recurrenceBonus = min(geometry.size * 0.3, 3.0)
    val impact = (baseImpactFor(categoryId) + recurrenceBonus).coerceIn(0.0, 10.0)

    val coordsArray = (geometry.firstOrNull()?.coordinates as? JsonArray)
    val lon = (coordsArray?.getOrNull(0) as? JsonPrimitive)?.doubleOrNull
    val lat = (coordsArray?.getOrNull(1) as? JsonPrimitive)?.doubleOrNull

    val risk = classifyRisk(severity, impact)

    return Event(
        id = id,
        title = title,
        description = description ?: "Sem descrição disponível.",
        category = categoryTitle,
        categoryId = categoryId,
        source = sources.firstOrNull()?.id ?: "EONET",
        mission = missionFor(categoryId),
        technology = technologyFor(categoryId),
        severity = severity,
        impact = impact,
        latitude = lat,
        longitude = lon,
        date = geometry.firstOrNull()?.date?.take(10) ?: "—",
        link = link.orEmpty(),
        riskLevel = risk,
        isAlert = risk == RiskLevel.ALTO || risk == RiskLevel.CRITICO
    )
}

private fun baseSeverityFor(categoryId: String): Int = when (categoryId) {
    "wildfires" -> 9
    "volcanoes" -> 10
    "severeStorms" -> 8
    "drought" -> 7
    "floods" -> 8
    "earthquakes" -> 9
    "landslides" -> 6
    "snow" -> 4
    "seaLakeIce" -> 3
    "dustHaze" -> 5
    "manmade" -> 5
    else -> 5
}

private fun baseImpactFor(categoryId: String): Double = when (categoryId) {
    "wildfires" -> 7.5
    "volcanoes" -> 8.5
    "severeStorms" -> 6.5
    "drought" -> 5.5
    "floods" -> 7.0
    "earthquakes" -> 7.5
    "landslides" -> 5.0
    "snow" -> 3.0
    "seaLakeIce" -> 2.5
    "dustHaze" -> 4.0
    "manmade" -> 4.5
    else -> 4.0
}

private fun missionFor(categoryId: String): String = when (categoryId) {
    "wildfires" -> "NASA FIRMS (MODIS/VIIRS)"
    "volcanoes" -> "Sentinel-2 / Landsat 8"
    "severeStorms" -> "GOES-16"
    "drought" -> "NASA SMAP"
    "floods" -> "Sentinel-1"
    "earthquakes" -> "USGS / Sentinel-1"
    "landslides" -> "Sentinel-1"
    else -> "EONET Aggregator"
}

private fun technologyFor(categoryId: String): String = when (categoryId) {
    "wildfires" -> "Imageamento Multiespectral"
    "volcanoes" -> "Imageamento Termal"
    "severeStorms" -> "Radar Meteorológico Orbital"
    "drought" -> "Radiometria de Umidade do Solo"
    "floods" -> "Radar de Abertura Sintética"
    "earthquakes" -> "Interferometria SAR"
    "landslides" -> "Radar de Abertura Sintética"
    else -> "Sensoriamento Remoto"
}