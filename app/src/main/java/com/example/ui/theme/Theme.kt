package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ModernLightColorScheme =
  lightColorScheme(
    primary = BlackMain,
    onPrimary = WhitePure,
    primaryContainer = Gray100,
    onPrimaryContainer = BlackMain,
    secondary = Orange500,
    onSecondary = WhitePure,
    secondaryContainer = Orange50,
    onSecondaryContainer = Orange900,
    tertiary = Orange500,
    onTertiary = WhitePure,
    background = WhitePure,
    onBackground = BlackMain,
    surface = WhitePure,
    onSurface = BlackMain,
    surfaceVariant = Gray50,
    onSurfaceVariant = Gray500,
    outline = Gray200,
    outlineVariant = Gray100
  )

private val ModernDarkColorScheme =
  darkColorScheme(
    primary = WhitePure,
    onPrimary = BlackMain,
    primaryContainer = Gray800,
    onPrimaryContainer = WhitePure,
    secondary = Orange500,
    onSecondary = WhitePure,
    secondaryContainer = Gray800,
    onSecondaryContainer = Orange100,
    tertiary = Orange400,
    onTertiary = BlackMain,
    background = BlackMain,
    onBackground = WhitePure,
    surface = Gray900,
    onSurface = WhitePure,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray400,
    outline = Gray700,
    outlineVariant = Gray800
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) ModernDarkColorScheme else ModernLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
