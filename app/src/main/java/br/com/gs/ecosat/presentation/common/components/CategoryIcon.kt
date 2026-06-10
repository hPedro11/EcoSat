package br.com.gs.ecosat.presentation.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Storm
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.gs.ecosat.presentation.theme.RiskCritical
import br.com.gs.ecosat.presentation.theme.RiskHigh
import br.com.gs.ecosat.presentation.theme.RiskLow
import br.com.gs.ecosat.presentation.theme.RiskMedium
import androidx.compose.ui.graphics.Color
import br.com.gs.ecosat.domain.model.RiskLevel

fun iconForCategory(categoryId: String): ImageVector = when (categoryId) {
    "wildfires" -> Icons.Filled.LocalFireDepartment
    "volcanoes" -> Icons.Filled.Terrain
    "severeStorms" -> Icons.Filled.Storm
    "drought" -> Icons.Filled.WbSunny
    "floods" -> Icons.Filled.Water
    "earthquakes" -> Icons.Filled.Public
    "landslides" -> Icons.Filled.Terrain
    "snow" -> Icons.Filled.AcUnit
    "dustHaze" -> Icons.Filled.Air
    "manmade" -> Icons.Filled.Forest
    else -> Icons.Filled.Forest
}

fun colorForRisk(level: RiskLevel): Color = when (level) {
    RiskLevel.CRITICO -> RiskCritical
    RiskLevel.ALTO -> RiskHigh
    RiskLevel.MEDIO -> RiskMedium
    RiskLevel.BAIXO -> RiskLow
}