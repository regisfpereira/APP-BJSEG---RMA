package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RmaItem
import com.example.model.RmaStatus
import com.example.model.RmaTimelineEvent
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
import com.example.ui.theme.Gray800
import com.example.ui.theme.Orange100
import com.example.ui.theme.Orange200
import com.example.ui.theme.Orange50
import com.example.ui.theme.Orange500
import com.example.ui.theme.Orange700
import com.example.ui.theme.WhitePure

@Composable
fun RmaDetailScreen(
    rma: RmaItem,
    uiState: BjsegUiState,
    viewModel: BjsegViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    BackHandler {
        viewModel.navigateBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhitePure)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // =========================================================================
        // 1. BLOCO DE IDENTIFICAÇÃO DO PRODUTO (Foto/ícone circular, nome, contatos/ações)
        // =========================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("os_digital_header_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WhitePure),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(1.dp, Gray200), CircleShape)
                            .background(Gray100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = BlackMain,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = rma.productName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${rma.rmaNumber} • Cód: ${rma.productCode}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                        )
                        if (!rma.serialNumber.isNullOrBlank()) {
                            Text(
                                text = "S/N: ${rma.serialNumber}",
                                style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Linha de botões circulares de ação (WhatsApp, Telefone, Mapa, Compartilhar)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularActionButton(
                        icon = Icons.Default.SupportAgent,
                        contentDescription = "WhatsApp Pós-Vendas",
                        onClick = {
                            val url = viewModel.getWhatsAppUrl(rma.rmaNumber, isPayment = rma.isPendingPayment)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )

                    CircularActionButton(
                        icon = Icons.Outlined.Phone,
                        contentDescription = "Ligar para Unidade",
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${rma.store.phone.filter { it.isDigit() }}"))
                            context.startActivity(intent)
                        }
                    )

                    CircularActionButton(
                        icon = Icons.Outlined.LocationOn,
                        contentDescription = "Ver Loja no Mapa",
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rma.store.webMapUrl))
                            context.startActivity(intent)
                        }
                    )

                    CircularActionButton(
                        icon = Icons.Outlined.Share,
                        contentDescription = "Compartilhar dados da OS",
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Acompanhamento de OS BJSEG: ${rma.rmaNumber} - ${rma.productName} - Status: ${rma.status.clientTitle}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Compartilhar OS"))
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    RmaStatusMinimalBadge(status = rma.status)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Alerta de pagamento pendente (se houver)
        if (rma.status == RmaStatus.AGUARDANDO_PAGAMENTO) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_detail_payment_action"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Orange50),
                border = BorderStroke(1.dp, Orange200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Etapa de Pagamento Pendente",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Há uma etapa de pagamento pendente para dar continuidade ao atendimento. Entre em contato com o Pós-Vendas para receber as orientações.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray700)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val url = viewModel.getWhatsAppUrl(rma.rmaNumber, isPayment = true)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlackMain,
                            contentColor = WhitePure
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("btn_detail_pay_now")
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pagar agora (WhatsApp)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // =========================================================================
        // 2. INFORMAÇÕES DA OS EM CARTÃO DE GRADE DE 2 COLUNAS
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WhitePure),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DADOS DA ORDEM DE SERVIÇO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gray500,
                        letterSpacing = 0.5.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Linha 1 da grade: Número e Status
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NÚMERO DA OS",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rma.rmaNumber,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(38.dp).background(Gray200))
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "STATUS ATUAL",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rma.status.clientTitle,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Gray200, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Linha 2 da grade: Tipo de atendimento e Cliente
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TIPO DE ATENDIMENTO",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rma.rmaType.displayName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(38.dp).background(Gray200))
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CLIENTE TITULAR",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rma.customerName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            ),
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Gray200, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Linha 3 da grade: Data de abertura e Previsão estimada (30 dias corridos)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DATA DE ABERTURA",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rma.openedDateFormatted,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(38.dp).background(Gray200))
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PREVISÃO ESTIMADA",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rma.estimatedDeadlineDisplay,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Orange700
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Card de defeito relatado e laudo
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WhitePure),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DEFEITO INFORMADO PELO CLIENTE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gray500,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Gray50,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Gray200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\"${rma.customerDefect}\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = BlackMain
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }

                if (!rma.customerNotes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "OBSERVAÇÕES DO LAUDO TÉCNICO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gray500,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = rma.customerNotes,
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray700)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Card da Unidade / Loja de retirada
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("os_store_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WhitePure),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "UNIDADE DE RETIRADA",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gray500,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rma.store.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }

                    CircularActionButton(
                        icon = Icons.Outlined.LocationOn,
                        contentDescription = "Ver no mapa",
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rma.store.webMapUrl))
                            context.startActivity(intent)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = rma.store.fullAddress,
                    style = MaterialTheme.typography.bodySmall.copy(color = Gray600)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Horário: ${rma.store.openingHours}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bloco de avaliação e crédito (se finalizado)
        if (rma.isRated) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Gray50),
                border = BorderStroke(1.dp, Gray200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Orange500, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Atendimento Avaliado (${rma.ratingStars} estrelas)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BlackMain)
                        )
                    }
                    if (!rma.ratingComment.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${rma.ratingComment}\"",
                            style = MaterialTheme.typography.bodySmall.copy(color = Gray600)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Crédito de R$ 0,01 registrado na sua carteira.",
                        style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        } else if (rma.isEligibleForRating) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_evaluate_invitation"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Orange50),
                border = BorderStroke(1.dp, Orange200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Orange500, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Avalie este Atendimento",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BlackMain)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Avalie seu atendimento e receba R$ 0,01 em créditos para utilizar como desconto em futuras compras na BJSEG.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray700)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.openRatingScreen(rma.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = BlackMain, contentColor = WhitePure),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(40.dp).testTag("btn_evaluate_rma")
                    ) {
                        Text("Avaliar Atendimento (+R$ 0,01)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // =========================================================================
        // 3. LINHA DO TEMPO VERTICAL (Ícones circulares, data/hora e status com checkbox laranja)
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WhitePure),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Histórico de Atualizações",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        )
                    )

                    Surface(
                        color = Gray100,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${rma.timeline.size} eventos",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Gray700
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                rma.timeline.forEachIndexed { index, event ->
                    TimelineEventMinimalRow(
                        event = event,
                        isFirst = index == 0,
                        isLast = index == rma.timeline.size - 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botão secundário de contato via WhatsApp
        Button(
            onClick = {
                val url = viewModel.getWhatsAppUrl(rma.rmaNumber, isPayment = false)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("btn_whatsapp_rma_support"),
            colors = ButtonDefaults.buttonColors(
                containerColor = BlackMain,
                contentColor = WhitePure
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Falar com Pós-Vendas sobre este RMA", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun TimelineEventMinimalRow(
    event: RmaTimelineEvent,
    isFirst: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isFirst) Orange500 else Gray100)
                    .border(BorderStroke(1.dp, if (isFirst) Orange500 else Gray300), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isFirst) {
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
                            .background(Gray500)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .height(48.dp)
                        .background(Gray200)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isFirst) FontWeight.Bold else FontWeight.Medium,
                        color = BlackMain
                    )
                )
                Text(
                    text = event.dateDisplay,
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = event.explanation,
                style = MaterialTheme.typography.bodySmall.copy(color = Gray600)
            )

            if (!event.additionalInfo.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = event.additionalInfo,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = Gray700
                    )
                )
            }
        }
    }
}
