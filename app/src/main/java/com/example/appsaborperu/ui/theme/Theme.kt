package com.example.appsaborperu.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Tipografía personalizada
private val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

// Esquema de colores oscuro - Bandera de Perú
private val DarkColorScheme = darkColorScheme(
    primary = Red80,
    onPrimary = Color.Black,
    primaryContainer = PeruRedDark,
    onPrimaryContainer = Color.White,
    secondary = RedGrey80,
    tertiary = RedAccent80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onBackground = Color.White,
    onSurface = Color.White
)

// Esquema de colores claro - Bandera de Perú 🇵🇪
private val LightColorScheme = lightColorScheme(
    primary = PeruRed,                    // Rojo peruano principal
    onPrimary = Color.White,              // Texto blanco sobre rojo
    primaryContainer = PeruRedLight,      // Contenedor rojo claro
    onPrimaryContainer = Color.White,     // Texto blanco
    secondary = RedGrey40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE4E6),
    onSecondaryContainer = PeruRedDark,
    tertiary = RedAccent40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    error = Color(0xFFB3261E),
    errorContainer = Color(0xFFF9DEDC)
)

@Composable
fun AppSaborPeruTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Deshabilitamos dynamic color para usar nuestros colores personalizados
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
