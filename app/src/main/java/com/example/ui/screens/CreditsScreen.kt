package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CreditTransaction
import com.example.ui.BjsegUiState
import com.example.ui.BjsegViewModel
import com.example.ui.theme.BlackMain
import com.example.ui.theme.Gray100
import com.example.ui.theme.Gray200
import com.example.ui.theme.Gray300
import com.example.ui.theme.Gray50
import com.example.ui.theme.Gray500
import com.example.ui.theme.Gray600
import com.example.ui.theme.Gray700
import com.example.ui.theme.Orange100
import com.example.ui.theme.Orange50
import com.example.ui.theme.Orange500
import com.example.ui.theme.Orange700
import com.example.ui.theme.WhitePure

@Composable
fun CreditsScreen(
    uiState: BjsegUiState,
    viewModel: BjsegViewModel,
    modifier: Modifier = Modifier
) {
    val wallet = uiState.creditWallet

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WhitePure)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // =========================================================================
        // 1. CARTÃO DE PROGRESSO (Porcentagem, barra Laranja 500 com marcador preto, restante Laranja 100)
        // =========================================================================
        item {
            val progressFraction = wallet?.progressFraction ?: 0f
            val percentageInt = (progressFraction * 100).toInt()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("credits_wallet_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                border = BorderStroke(1.dp, Gray200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CARTEIRA DE CRÉDITOS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Gray500,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = wallet?.formattedBalance ?: "R$ 0,00",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }

                        // Badge com porcentagem
                        Surface(
                            color = Orange50,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Orange100)
                        ) {
                            Text(
                                text = "$percentageInt%",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Orange700
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Barra de progresso Laranja 500 com marcador circular preto e restante Laranja 100
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        // Trecho restante em Laranja 100
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Orange100)
                        )

                        // Trecho preenchido em Laranja 500
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = progressFraction.coerceIn(0.02f, 1f))
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Orange500)
                        )

                        // Marcador circular preto
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = progressFraction.coerceIn(0.04f, 1f)),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(BlackMain)
                                    .border(BorderStroke(1.5.dp, WhitePure), CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (wallet?.canUseDiscount == true) {
                            "Você atingiu o saldo mínimo de R$ 5,00 para utilizar seus créditos como desconto em futuras compras na BJSEG."
                        } else {
                            "Continue avaliando seus atendimentos para acumular créditos. Você poderá utilizá-los como desconto quando atingir R$ 5,00. Faltam ${wallet?.formattedRemaining ?: "R$ 5,00"}."
                        },
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray600)
                    )
                }
            }
        }

        // =========================================================================
        // 2. INFORMAÇÕES EM CARTÃO DE GRADE DE 2 COLUNAS
        // =========================================================================
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                border = BorderStroke(1.dp, Gray200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "MÍNIMO DE RESGATE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Gray500,
                                    letterSpacing = 0.4.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = wallet?.formattedThreshold ?: "R$ 5,00",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }

                        Box(modifier = Modifier.width(1.dp).height(36.dp).background(Gray200))
                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "VALOR RESTANTE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Gray500,
                                    letterSpacing = 0.4.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = wallet?.formattedRemaining ?: "R$ 5,00",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Gray200, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "GANHO POR AVALIAÇÃO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Gray500,
                                    letterSpacing = 0.4.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "+ R$ 0,01 por OS",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Orange700
                                )
                            )
                        }

                        Box(modifier = Modifier.width(1.dp).height(36.dp).background(Gray200))
                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "DESTINAÇÃO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Gray500,
                                    letterSpacing = 0.4.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Desconto em compras",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }
                    }
                }
            }
        }

        // =========================================================================
        // 3. REGRAS DO PROGRAMA (Moderno, minimalista, ícones finos)
        // =========================================================================
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                border = BorderStroke(1.dp, Gray200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Regras do Programa",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RuleMinimalItem(
                        title = "R$ 0,01 por avaliação realizada",
                        description = "Qualquer avaliação válida (de 1 a 5 estrelas) gera R$ 0,01 em créditos na sua conta."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RuleMinimalItem(
                        title = "Desconto comercial na BJSEG",
                        description = "Os créditos acumulam e são aplicados como abatimento em novos pedidos de equipamentos."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RuleMinimalItem(
                        title = "Sem saque ou transferência Pix",
                        description = "Os créditos não podem ser convertidos em dinheiro nem transferidos para contas bancárias."
                    )
                }
            }
        }

        // =========================================================================
        // 4. EXTRATO DE MOVIMENTAÇÕES (Badge laranja, lista de transações)
        // =========================================================================
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Extrato de Movimentações",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = Orange500,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = (wallet?.transactions?.size ?: 0).toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        val txs = wallet?.transactions.orEmpty()
        if (txs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = WhitePure),
                    border = BorderStroke(1.dp, Gray200),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhum crédito acumulado no momento.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                        )
                    }
                }
            }
        } else {
            items(txs, key = { it.id }) { tx ->
                CreditTransactionMinimalCard(transaction = tx)
            }
        }
    }
}

@Composable
fun RuleMinimalItem(
    title: String,
    description: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(Orange500)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BlackMain
                )
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
            )
        }
    }
}

@Composable
fun CreditTransactionMinimalCard(
    transaction: CreditTransaction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tx_card_${transaction.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WhitePure),
        border = BorderStroke(1.dp, Gray200),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = transaction.type,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BlackMain
                    )
                )

                Surface(
                    color = Orange50,
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Orange100)
                ) {
                    Text(
                        text = transaction.formattedAmount,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Orange700
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodySmall.copy(color = Gray600)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = transaction.dateTimeDisplay,
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                )

                Text(
                    text = transaction.status,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = BlackMain
                    )
                )
            }
        }
    }
}
