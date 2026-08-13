package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val UsPlayDarkColorScheme = darkColorScheme(
    primary = UsPlayRosePrimary,
    onPrimary = Color.White,
    primaryContainer = UsPlayRoseLight,
    onPrimaryContainer = UsPlayTextPrimary,
    secondary = UsPlayCoralAccent,
    onSecondary = Color.White,
    tertiary = UsPlayGoldXP,
    onTertiary = Color.White,
    background = UsPlayPlumBackground,
    onBackground = UsPlayTextPrimary,
    surface = UsPlayPlumCard,
    onSurface = UsPlayTextPrimary,
    surfaceVariant = UsPlayPlumCardElevated,
    onSurfaceVariant = UsPlayTextSecondary
)

private val UsPlayLightColorScheme = lightColorScheme(
    primary = UsPlayRosePrimary,
    onPrimary = Color.White,
    primaryContainer = UsPlayRoseLight,
    onPrimaryContainer = UsPlayTextPrimary,
    secondary = UsPlayCoralAccent,
    onSecondary = Color.White,
    tertiary = UsPlayGoldXP,
    onTertiary = Color.White,
    background = UsPlayPlumBackground,
    onBackground = UsPlayTextPrimary,
    surface = UsPlayPlumCard,
    onSurface = UsPlayTextPrimary,
    surfaceVariant = UsPlayPlumCardElevated,
    onSurfaceVariant = UsPlayTextSecondary
)

@Composable
fun UsPlayTheme(
    darkTheme: Boolean = true, // Default to dark/vibrant theme for UsPlay
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) UsPlayDarkColorScheme else UsPlayLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

