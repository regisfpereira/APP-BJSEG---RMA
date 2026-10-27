package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// =========================================================================
// PALETA OFICIAL EXCLUSIVA (Apenas Laranja, Cinza, Preto e Branco)
// =========================================================================

// Laranja
val Orange50 = Color(0xFFFFF4EB)
val Orange100 = Color(0xFFFFE3CC)
val Orange200 = Color(0xFFFFC799)
val Orange300 = Color(0xFFFFA866)
val Orange400 = Color(0xFFFF8A33)
val Orange500 = Color(0xFFFF6A00) // Cor de destaque principal
val Orange600 = Color(0xFFE55F00)
val Orange700 = Color(0xFFBF4F00)
val Orange800 = Color(0xFF993F00)
val Orange900 = Color(0xFF732F00)

// Cinza
val Gray50 = Color(0xFFFAFAFA)
val Gray100 = Color(0xFFF5F5F5)
val Gray200 = Color(0xFFE5E5E5) // Bordas finas
val Gray300 = Color(0xFFD4D4D4)
val Gray400 = Color(0xFFA3A3A3)
val Gray500 = Color(0xFF6B6B6B) // Rótulos secundários
val Gray600 = Color(0xFF525252)
val Gray700 = Color(0xFF404040)
val Gray800 = Color(0xFF262626)
val Gray900 = Color(0xFF171717)

// Preto
val BlackMain = Color(0xFF111111) // Preto principal para textos e botões
val BlackPure = Color(0xFF000000)

// Branco
val WhitePure = Color(0xFFFFFFFF) // Fundo e cartões

// Aliases para compatibilidade semântica com o tema
val BjsegOrange = Orange500
val BjsegOrangeLight = Orange400
val BjsegOrangeDark = Orange700
val BjsegOrangeContainer = Orange100
val BjsegOnOrangeContainer = BlackMain

val BjsegGraphite = BlackMain
val BjsegGraphiteLight = Gray800
val BjsegGraphiteDark = BlackPure

val BjsegGraySurface = WhitePure
val BjsegGrayCard = WhitePure
val BjsegGrayBorder = Gray200
val BjsegGrayTextSecondary = Gray500

val BjsegStatusOpen = Gray800
val BjsegStatusAnalysis = Gray700
val BjsegStatusSupplier = Gray600
val BjsegStatusPayment = Orange600
val BjsegStatusReturning = Orange500
val BjsegStatusReady = BlackMain
val BjsegStatusExchange = BlackMain

val BjsegGold = Orange500
