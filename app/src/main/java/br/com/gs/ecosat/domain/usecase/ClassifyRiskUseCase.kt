package br.com.gs.ecosat.domain.usecase

import br.com.gs.ecosat.domain.model.RiskLevel

class ClassifyRiskUseCase {
    operator fun invoke(severity: Int, impact: Double): RiskLevel {
        return when {
            severity >= 9 || impact >= 8.0 -> RiskLevel.CRITICO
            severity in 7..8 || impact in 6.0..7.99 -> RiskLevel.ALTO
            severity in 4..6 || impact in 4.0..5.99 -> RiskLevel.MEDIO
            else -> RiskLevel.BAIXO
        }
    }
}