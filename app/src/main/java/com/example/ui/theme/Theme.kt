package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = RoyalBlue,
    onPrimary = Color.White,
    secondary = AccentOrange,
    onSecondary = Color.White,
    tertiary = RoyalBlueLight,
    background = DarkNavy,
    surface = Color(0xFF1E293B),
    onBackground = Color.White,
    onSurface = Color.White,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RoyalBlue,
    onPrimary = Color.White,
    primaryContainer = RoyalBlueLight,
    onPrimaryContainer = RoyalBlueDark,
    secondary = AccentOrange,
    onSecondary = Color.White,
    secondaryContainer = AccentOrangeLight,
    onSecondaryContainer = DarkNavy,
    tertiary = DarkNavy,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F4F9),
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
