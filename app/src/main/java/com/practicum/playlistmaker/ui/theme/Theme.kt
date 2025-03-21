package com.practicum.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.practicum.playlistmaker.R


private val LightColors = lightColorScheme(
    primary = LightDark,
    secondary = Grey,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = LightGrey,
    surfaceVariant = LightGrey,
    onSurfaceVariant = Grey
)

private val DarkColors = darkColorScheme(
    primary = Color.White,
    secondary = Color.White,
    background = LightDark,
    surface = LightDark,
    onPrimary = LightDark,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = Color.White,
    surfaceVariant = Color.White,
    onSurfaceVariant = LightDark
)

val YsDisplayFont = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_medium, FontWeight.Medium)
)



@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}