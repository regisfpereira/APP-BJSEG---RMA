package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = BjsegOrangeLight,
    onPrimary = Color.White,
    primaryContainer = BjsegOrangeDark,
    onPrimaryContainer = Color.White,
    secondary = BjsegOrange,
    onSecondary = Color.White,
    tertiary = BjsegGold,
    background = BjsegGraphiteDark,
    surface = BjsegGraphite,
    surfaceVariant = BjsegGraphiteLight,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF3E4756)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BjsegOrange,
    onPrimary = Color.White,
    primaryContainer = BjsegOrangeContainer,
    onPrimaryContainer = BjsegOnOrangeContainer,
    secondary = BjsegGraphite,
    onSecondary = Color.White,
    tertiary = BjsegGold,
    background = BjsegGraySurface,
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = BjsegGrayTextSecondary,
    outline = BjsegGrayBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Set to false to preserve strict BJSEG corporate identity
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
