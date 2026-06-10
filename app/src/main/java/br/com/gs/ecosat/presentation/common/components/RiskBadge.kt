package br.com.gs.ecosat.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.gs.ecosat.domain.model.RiskLevel
import br.com.gs.ecosat.presentation.theme.RiskCritical
import br.com.gs.ecosat.presentation.theme.RiskHigh
import br.com.gs.ecosat.presentation.theme.RiskLow
import br.com.gs.ecosat.presentation.theme.RiskMedium

@Composable
fun RiskBadge(level: RiskLevel, modifier: Modifier = Modifier) {
    val color = when (level) {
        RiskLevel.CRITICO -> RiskCritical
        RiskLevel.ALTO -> RiskHigh
        RiskLevel.MEDIO -> RiskMedium
        RiskLevel.BAIXO -> RiskLow
    }
    Text(
        text = level.label.uppercase(),
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .background(color, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}