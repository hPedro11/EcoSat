package br.com.gs.ecosat.domain.model

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val categoryId: String,
    val source: String,
    val mission: String,
    val technology: String,
    val severity: Int,           // 0-10
    val impact: Double,          // 0.0-10.0
    val latitude: Double?,
    val longitude: Double?,
    val date: String,
    val link: String,
    val riskLevel: RiskLevel,
    val isAlert: Boolean         // true se ALTO ou CRITICO
)