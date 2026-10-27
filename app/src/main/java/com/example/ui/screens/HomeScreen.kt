package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RmaItem
import com.example.model.RmaStatus
import com.example.ui.BjsegUiState
import com.example.ui.BjsegViewModel
import com.example.ui.MainNavTab
import com.example.ui.RmaFilterCategory
import com.example.ui.theme.BlackMain
import com.example.ui.theme.Gray100
import com.example.ui.theme.Gray200
import com.example.ui.theme.Gray300
import com.example.ui.theme.Gray50
import com.example.ui.theme.Gray500
import com.example.ui.theme.Gray600
import com.example.ui.theme.Gray700
import com.example.ui.theme.Gray800
import com.example.ui.theme.Orange100
import com.example.ui.theme.Orange200
import com.example.ui.theme.Orange50
import com.example.ui.theme.Orange500
import com.example.ui.theme.Orange700
import com.example.ui.theme.WhitePure

@Composable
fun HomeScreen(
    uiState: BjsegUiState,
    viewModel: BjsegViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val activeRmas = uiState.rmas.filter { !it.status.isFinished }
    val finishedRmas = uiState.rmas.filter { it.status.isFinished }
    val wallet = uiState.creditWallet

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhitePure)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // =========================================================================
        // 1. BLOCO DE IDENTIFICAÇÃO (Foto circular, nome, contato, botões circulares)
        // =========================================================================
        val customerName = uiState.currentUser?.name ?: "Cliente BJSEG"
        val userPhone = uiState.currentUser?.phone ?: "(11) 4646-7000"
        val userEmail = uiState.currentUser?.email ?: "atendimento@bjseg.com.br"
        val initials = customerName.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercase() }
            .joinToString("")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("identification_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WhitePure),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Foto / avatar circular com contorno fino cinza
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(1.dp, Gray200), CircleShape)
                            .background(Gray100),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (initials.isNotBlank()) initials else "BJ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = customerName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            ),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${uiState.currentUser?.maskedDocument ?: "Documento verificado"} • $userPhone",
                            style = MaterialTheme.typography.bodySmall.copy(color = Gray500),
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Linha de botões circulares secundários (contorno fino cinza 200, ícone preto)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CircularActionButton(
                        icon = Icons.Outlined.Phone,
                        contentDescription = "Ligar",
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${viewModel.uiState.value.whatsAppNumber}"))
                            context.startActivity(intent)
                        }
                    )

                    CircularActionButton(
                        icon = Icons.Default.SupportAgent,
                        contentDescription = "WhatsApp Pós-Vendas",
                        onClick = {
                            val url = viewModel.getGeneralWhatsAppUrl()
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )

                    CircularActionButton(
                        icon = Icons.Outlined.Email,
                        contentDescription = "E-mail de Suporte",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:posvendas@bjseg.com.br"))
                            context.startActivity(intent)
                        }
                    )

                    CircularActionButton(
                        icon = Icons.Outlined.LocationOn,
                        contentDescription = "Unidades e Lojas",
                        onClick = {
                            viewModel.onTabSelected(MainNavTab.PERFIL)
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Botão principal pequeno, preto, texto branco, cantos levemente arredondados
                    Button(
                        onClick = { viewModel.onTabSelected(MainNavTab.MEUS_RMAS) },
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("home_track_rmas_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlackMain,
                            contentColor = WhitePure
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Meus RMAs",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = WhitePure
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =========================================================================
        // 2. INFORMAÇÕES EM CARTÃO DE GRADE DE 2 COLUNAS (Rótulo cinza pequeno e valor em negrito)
        // =========================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("overview_grid_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WhitePure),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.onFilterSelected(RmaFilterCategory.EM_ANDAMENTO)
                                viewModel.onTabSelected(MainNavTab.MEUS_RMAS)
                            }
                    ) {
                        Text(
                            text = "ORDENS ATIVAS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Gray500,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${activeRmas.size} em andamento",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Gray200)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.onFilterSelected(RmaFilterCategory.FINALIZADOS)
                                viewModel.onTabSelected(MainNavTab.MEUS_RMAS)
                            }
                    ) {
                        Text(
                            text = "FINALIZADOS / TROCAS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Gray500,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${finishedRmas.size} concluídos",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Gray200, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ATENDIMENTO PRINCIPAL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Gray500,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Itaquaquecetuba (CD)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Gray200)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CANAL DE CONTATO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Gray500,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "WhatsApp Oficial",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =========================================================================
        // 3. CARTÃO DE PROGRESSO (Porcentagem, barra Laranja 500 com marcador preto, restante em Laranja 100)
        // =========================================================================
        if (wallet != null) {
            val progressFraction = wallet.progressFraction
            val percentageInt = (progressFraction * 100).toInt()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onTabSelected(MainNavTab.CREDITOS) }
                    .testTag("dashboard_progress_card"),
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
                                text = "PROGRAMA MEUS CRÉDITOS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Gray500,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = wallet.formattedBalance,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }

                        // Badge com porcentagem em destaque
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Barra de progresso personalizada: Laranja 500, marcador circular preto, fundo Laranja 100
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        // Track restante em Laranja 100
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Orange100)
                        )

                        // Progresso preenchido em Laranja 500
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (wallet.canUseDiscount) {
                                "Meta de R$ 5,00 atingida para desconto!"
                            } else {
                                "Meta: ${wallet.formattedThreshold} (faltam ${wallet.formattedRemaining})"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                        )

                        Text(
                            text = "+R$ 0,01 por avaliação",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Orange700
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // =========================================================================
        // 4. AVISOS DE ATENÇÃO (Aguardando Pagamento & Retirada) — Laranja e Preto
        // =========================================================================
        if (uiState.urgentPaymentRma != null) {
            val paymentRma = uiState.urgentPaymentRma
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("urgent_payment_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Orange50),
                border = BorderStroke(1.dp, Orange200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Orange500)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Aguardando Pagamento",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }

                        Surface(
                            color = WhitePure,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Orange200)
                        ) {
                            Text(
                                text = paymentRma.rmaNumber,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Orange700
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${paymentRma.productName}\nHá uma etapa de pagamento pendente. Entre em contato com o Pós-Vendas para receber as orientações.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray700)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.openRmaDetail(paymentRma.id) },
                            modifier = Modifier
                                .height(38.dp)
                                .padding(end = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Gray300)
                        ) {
                            Text("Ver OS", color = BlackMain, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val url = viewModel.getWhatsAppUrl(paymentRma.rmaNumber, isPayment = true)
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BlackMain,
                                contentColor = WhitePure
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("home_pay_now_button")
                        ) {
                            Text("Pagar agora (WhatsApp)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (uiState.urgentPickupRma != null) {
            val pickupRma = uiState.urgentPickupRma
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("urgent_pickup_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Gray50),
                border = BorderStroke(1.dp, Gray200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(BlackMain)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Disponível para Retirada",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }

                        Surface(
                            color = Orange50,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Orange100)
                        ) {
                            Text(
                                text = "Loja ${pickupRma.store.name.split("—").first().trim()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Orange700
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${pickupRma.productName}\n${pickupRma.store.fullAddress}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray600)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pickupRma.store.webMapUrl))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .height(38.dp)
                                .padding(end = 8.dp)
                                .testTag("home_pickup_map_button"),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Gray300)
                        ) {
                            Text("Ver no mapa", color = BlackMain, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.openRmaDetail(pickupRma.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BlackMain,
                                contentColor = WhitePure
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Text("Ver Detalhes", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // =========================================================================
        // 5. LISTA DE ATIVIDADES / ATENDIMENTOS (Badge laranja, "Ver todas", linha do tempo)
        // =========================================================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Atividades Recentes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BlackMain
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Badge laranja 500 de contagem
                Surface(
                    color = Orange500,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = uiState.rmas.size.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = "Ver todas",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BlackMain
                ),
                modifier = Modifier
                    .clickable { viewModel.onTabSelected(MainNavTab.MEUS_RMAS) }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Linha do tempo vertical com ícones circulares, data/hora e checkbox laranja
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WhitePure),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (uiState.rmas.isEmpty()) {
                    Text(
                        text = "Você ainda não possui atendimentos registrados.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray500),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    uiState.rmas.take(4).forEachIndexed { index, rma ->
                        TimelineActivityRow(
                            rma = rma,
                            isLast = index == minOf(3, uiState.rmas.size - 1),
                            onClick = { viewModel.openRmaDetail(rma.id) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun CircularActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(BorderStroke(1.dp, Gray200), CircleShape)
            .background(WhitePure)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = BlackMain,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun TimelineActivityRow(
    rma: RmaItem,
    isLast: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Marcador circular e linha vertical
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (rma.status.isFinished) Orange500 else Gray100)
                    .border(BorderStroke(1.dp, if (rma.status.isFinished) Orange500 else Gray300), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (rma.status.isFinished) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = WhitePure,
                        modifier = Modifier.size(14.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Orange500)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .height(44.dp)
                        .background(Gray200)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Conteúdo
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rma.rmaNumber,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BlackMain
                    )
                )

                Text(
                    text = rma.openedDateFormatted,
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = rma.productName,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = BlackMain
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Gray100,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = rma.status.clientTitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (rma.isPendingPayment) Orange700 else BlackMain
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = rma.store.name.split("—").first().trim(),
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                )
            }
        }
    }
}
