package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.ui.theme.BjsegGraphite
import com.example.ui.theme.BjsegOrange
import com.example.ui.theme.BjsegOrangeLight

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
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // 1. Dashboard Header: Greeting & Profile Verification
        val customerName = uiState.currentUser?.name ?: "Cliente"
        val firstName = if (customerName.contains(" ")) customerName.split(" ").first() else customerName

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dashboard_header"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Painel de Pós-Vendas",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BjsegOrange,
                        letterSpacing = 0.8.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Olá, $firstName!",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${uiState.currentUser?.maskedDocument ?: "Documento verificado"} • ${if (uiState.currentUser?.isCompany == true) "Conta Corporativa" else "Cliente Residencial"}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Urgent Attention Cards (Payment Pending or Pickup Ready)
        if (uiState.urgentPaymentRma != null) {
            val paymentRma = uiState.urgentPaymentRma
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("urgent_payment_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFCA5A5)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Ação Necessária: Aguardando Pagamento",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                            )
                            Text(
                                text = "${paymentRma.rmaNumber} • ${paymentRma.productName}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFB91C1C)),
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Existe uma etapa de pagamento pendente para prosseguir com a manutenção tabelada. Entre em contato com o Pós-Vendas para receber as orientações.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF7F1D1D))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        OutlinedButton(
                            onClick = { viewModel.openRmaDetail(paymentRma.id) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Ver OS", color = Color(0xFF991B1B))
                        }

                        Button(
                            onClick = {
                                val url = viewModel.getWhatsAppUrl(paymentRma.rmaNumber, isPayment = true)
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            modifier = Modifier.testTag("home_pay_now_button")
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pagar agora (WhatsApp)", fontWeight = FontWeight.Bold)
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
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF86EFAC)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF16A34A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Store, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Item Disponível para Retirada",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            )
                            Text(
                                text = "${pickupRma.rmaNumber} • ${pickupRma.productName}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF15803D)),
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Seu produto já chegou à loja e está disponível para entrega:\n• Unidade: ${pickupRma.store.name}\n• Endereço: ${pickupRma.store.fullAddress}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF14532D))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pickupRma.store.webMapUrl))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.padding(end = 8.dp).testTag("home_pickup_map_button")
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF166534))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ver no mapa", color = Color(0xFF166534))
                        }

                        Button(
                            onClick = { viewModel.openRmaDetail(pickupRma.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                        ) {
                            Text("Ver Detalhes da OS", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 3. User Credit Balance Card ("Meus Créditos")
        if (wallet != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onTabSelected(MainNavTab.CREDITOS) }
                    .testTag("dashboard_credits_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BjsegGraphite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(BjsegOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "PROGRAMA MEUS CRÉDITOS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFBBF24),
                                        letterSpacing = 0.8.sp
                                    )
                                )
                                Text(
                                    text = "Desconto comercial em compras na BJSEG",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8))
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Abrir Carteira de Créditos",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Saldo acumulado",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFCBD5E1))
                            )
                            Text(
                                text = wallet.formattedBalance,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFBBF24),
                                    fontSize = 32.sp
                                )
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Mínimo para utilizar",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFCBD5E1))
                            )
                            Text(
                                text = wallet.formattedThreshold,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { wallet.progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFFFBBF24),
                        trackColor = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (wallet.canUseDiscount) {
                                "Saldo mínimo atingido para desconto em compras!"
                            } else {
                                "Faltam ${wallet.formattedRemaining} para atingir o mínimo."
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (wallet.canUseDiscount) Color(0xFF4ADE80) else Color(0xFFCBD5E1)
                            )
                        )

                        Text(
                            text = "+R$ 0,01 por avaliação",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFBBF24)
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Active Service Orders & Status Overview Matrix
        Text(
            text = "Resumo das Ordens de Serviço (RMA)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Acompanhamento em tempo real dos seus atendimentos",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // KPI Counter Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DashboardKpiCard(
                title = "Ativas no Pós-Vendas",
                count = activeRmas.size,
                icon = Icons.Default.HourglassTop,
                iconTint = BjsegOrange,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.onFilterSelected(RmaFilterCategory.EM_ANDAMENTO)
                        viewModel.onTabSelected(MainNavTab.MEUS_RMAS)
                    }
                    .testTag("kpi_active_rmas")
            )

            DashboardKpiCard(
                title = "Finalizados / Trocas",
                count = finishedRmas.size,
                icon = Icons.Default.CheckCircle,
                iconTint = Color(0xFF16A34A),
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.onFilterSelected(RmaFilterCategory.FINALIZADOS)
                        viewModel.onTabSelected(MainNavTab.MEUS_RMAS)
                    }
                    .testTag("kpi_finished_rmas")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal Status Breakdown Carousel / Chips
        Text(
            text = "Status Operacional dos Atendimentos",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        val statusScrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(statusScrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RmaStatus.entries.forEach { status ->
                val count = uiState.rmas.count { it.status == status }
                StatusMatrixChip(
                    status = status,
                    count = count,
                    onClick = {
                        viewModel.onTabSelected(MainNavTab.MEUS_RMAS)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 5. Active Service Orders (OS) List Preview
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ordens de Serviço Ativas (${activeRmas.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "Ver todas (${uiState.rmas.size})",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BjsegOrange
                ),
                modifier = Modifier
                    .clickable { viewModel.onTabSelected(MainNavTab.MEUS_RMAS) }
                    .padding(4.dp)
                    .testTag("btn_dashboard_view_all_rmas")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (activeRmas.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nenhum RMA em andamento no momento.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                    Text(
                        text = "Todos os seus atendimentos anteriores foram concluídos com sucesso.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            activeRmas.forEach { rma ->
                ActiveRmaDashboardCard(
                    rma = rma,
                    onViewOs = { viewModel.openRmaDetail(rma.id) },
                    onContactWhatsApp = {
                        val url = viewModel.getWhatsAppUrl(rma.rmaNumber, isPayment = rma.isPendingPayment)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6. Quick Action Navigation Bar
        Text(
            text = "Atalhos Rápidos",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.onTabSelected(MainNavTab.MEUS_RMAS) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("home_track_rmas_button"),
                colors = ButtonDefaults.buttonColors(containerColor = BjsegOrange),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Meus RMAs", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    val url = viewModel.getGeneralWhatsAppUrl()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("home_whatsapp_support_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(18.dp), tint = BjsegOrange)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Falar com Pós-Vendas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun DashboardKpiCard(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = iconTint
                )
            )
        }
    }
}

@Composable
fun StatusMatrixChip(
    status: RmaStatus,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        when (status) {
                            RmaStatus.NOVO -> Color(0xFF0284C7)
                            RmaStatus.CONFIRMADO -> Color(0xFF7C3AED)
                            RmaStatus.ENVIADO_AO_FORNECEDOR -> Color(0xFFD97706)
                            RmaStatus.AGUARDANDO_PAGAMENTO -> Color(0xFFDC2626)
                            RmaStatus.LIBERADO -> Color(0xFF0D9488)
                            RmaStatus.DISPONIVEL_RETIRADA -> Color(0xFF16A34A)
                            RmaStatus.TROCA -> Color(0xFF059669)
                        }
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = status.clientTitle,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                )
            }
        }
    }
}

@Composable
fun ActiveRmaDashboardCard(
    rma: RmaItem,
    onViewOs: () -> Unit,
    onContactWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("active_rma_dashboard_card_${rma.rmaNumber}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: RMA Number + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rma.rmaNumber,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BjsegOrange
                    )
                )

                RmaStatusBadge(status = rma.status)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = rma.productName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            if (rma.productCode.isNotBlank()) {
                Text(
                    text = "Cód: ${rma.productCode}",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explanation Box
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = rma.status.explanation,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Unit and Previsão Estimada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(14.dp), tint = BjsegGraphite)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rma.store.name.split("—").first().trim(),
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = BjsegOrange)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Prazo: ${rma.estimatedDeadlineDisplay}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = BjsegOrange
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewOs,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BjsegGraphite),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver minha OS", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onContactWhatsApp,
                    modifier = Modifier.height(42.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if (rma.isPendingPayment) Icons.Default.Payment else Icons.Default.SupportAgent,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (rma.isPendingPayment) Color(0xFFDC2626) else BjsegOrange
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (rma.isPendingPayment) "Pagar agora" else "WhatsApp",
                        fontSize = 12.sp,
                        color = if (rma.isPendingPayment) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun RmaStatusBadge(
    status: RmaStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        RmaStatus.NOVO -> Pair(Color(0xFFE0F2FE), Color(0xFF0369A1))
        RmaStatus.CONFIRMADO -> Pair(Color(0xFFEDE9FE), Color(0xFF6D28D9))
        RmaStatus.ENVIADO_AO_FORNECEDOR -> Pair(Color(0xFFFEF3C7), Color(0xFFB45309))
        RmaStatus.AGUARDANDO_PAGAMENTO -> Pair(Color(0xFFFEE2E2), Color(0xFFB91C1C))
        RmaStatus.LIBERADO -> Pair(Color(0xFFCCFBF1), Color(0xFF0F766E))
        RmaStatus.DISPONIVEL_RETIRADA -> Pair(Color(0xFFDCFCE7), Color(0xFF15803D))
        RmaStatus.TROCA -> Pair(Color(0xFFD1FAE5), Color(0xFF065F46))
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Text(
            text = status.clientTitle,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
