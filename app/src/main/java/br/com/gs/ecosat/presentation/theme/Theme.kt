package br.com.gs.ecosat.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = EcoBlueDark,
    onPrimary = Color.White,
    secondary = EcoBlue,
    onSecondary = Color.White,
    background = EcoBackground,
    onBackground = EcoOnSurface,
    surface = EcoSurface,
    onSurface = EcoOnSurface
)

private val DarkColors = darkColorScheme(
    primary = EcoBlueDark,
    onPrimary = Color.White,
    secondary = EcoBlueLight
)

@Composable
fun EcoSatTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}