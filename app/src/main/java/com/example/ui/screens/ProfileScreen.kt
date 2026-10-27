package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BjsegStore
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
fun ProfileScreen(
    uiState: BjsegUiState,
    viewModel: BjsegViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val user = uiState.currentUser

    var showEditContactDialog by remember { mutableStateOf(false) }
    var showStoresList by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val customerName = user?.name ?: "Cliente BJSEG"
    val initials = customerName.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first().uppercase() }
        .joinToString("")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhitePure)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // =========================================================================
        // 1. BLOCO DE IDENTIFICAÇÃO (Foto circular, nome, contatos, botões circulares)
        // =========================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_info_card"),
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
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (user?.isCompany == true) "Pessoa Jurídica (CNPJ)" else "Pessoa Física (CPF)",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Text(
                            text = user?.maskedDocument ?: "***",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Orange700
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Linha de botões circulares de ação
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularActionButton(
                        icon = Icons.Outlined.Phone,
                        contentDescription = "Ligar",
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${user?.phone?.filter { it.isDigit() }}"))
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
                        contentDescription = "Enviar E-mail",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${user?.email}"))
                            context.startActivity(intent)
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    OutlinedButton(
                        onClick = { showEditContactDialog = true },
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("btn_request_contact_update"),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Text("Atualizar contato", fontSize = 12.sp, color = BlackMain, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // 2. INFORMAÇÕES DO CADASTRO EM GRADE DE 2 COLUNAS
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
                    text = "DADOS CADASTRAIS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gray500,
                        letterSpacing = 0.5.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DOCUMENTO",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = user?.maskedDocument ?: "Não informado",
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
                            text = "TELEFONE",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = user?.phone ?: "Não informado",
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

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "E-MAIL",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = user?.email ?: "Não informado",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            ),
                            maxLines = 1
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(38.dp).background(Gray200))
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TIPO DE CLIENTE",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (user?.isCompany == true) "Corporativo (CNPJ)" else "Consumidor (CPF)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // 3. DIRETÓRIO DE UNIDADES BJSEG (Lista expansível com botões circulares de rota)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStoresList = !showStoresList },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Unidades e Lojas BJSEG",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackMain
                            )
                        )
                        Text(
                            text = "6 unidades cadastradas em SP",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                        )
                    }

                    Icon(
                        imageVector = if (showStoresList) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = BlackMain
                    )
                }

                if (showStoresList) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Gray200, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    BjsegStore.OFFICIAL_STORES.forEach { store ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                Text(
                                    text = store.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BlackMain
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = store.fullAddress,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Gray600)
                                )
                                Text(
                                    text = "Tel: ${store.phone}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                                )
                            }

                            CircularActionButton(
                                icon = Icons.Outlined.LocationOn,
                                contentDescription = "Ver no Google Maps",
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(store.webMapUrl))
                                    context.startActivity(intent)
                                }
                            )
                        }
                        HorizontalDivider(color = Gray100, thickness = 1.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Alternador de Perfis de Teste
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Gray50),
            border = BorderStroke(1.dp, Gray200),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Alternar Perfil (Demonstração)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BlackMain
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Selecione uma conta para validar os fluxos e dados do sistema:",
                    style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = user?.isCompany == true,
                        onClick = { viewModel.switchDemoUser(isCompany = true) },
                        label = { Text("SegurTech (CNPJ)") },
                        shape = RoundedCornerShape(6.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BlackMain,
                            selectedLabelColor = WhitePure,
                            containerColor = WhitePure,
                            labelColor = Gray700
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Gray200,
                            selectedBorderColor = BlackMain,
                            borderWidth = 1.dp,
                            enabled = true,
                            selected = user?.isCompany == true
                        )
                    )

                    FilterChip(
                        selected = user?.isCompany != true,
                        onClick = { viewModel.switchDemoUser(isCompany = false) },
                        label = { Text("João Silva (CPF)") },
                        shape = RoundedCornerShape(6.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BlackMain,
                            selectedLabelColor = WhitePure,
                            containerColor = WhitePure,
                            labelColor = Gray700
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Gray200,
                            selectedBorderColor = BlackMain,
                            borderWidth = 1.dp,
                            enabled = true,
                            selected = user?.isCompany != true
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Privacidade LGPD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPrivacyDialog = true }
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Lock, contentDescription = null, tint = Gray600, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Privacidade e Proteção de Dados (LGPD)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = BlackMain,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Botão de Sair em preto com contorno
        OutlinedButton(
            onClick = { viewModel.logout() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("btn_logout"),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Gray200)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp), tint = BlackMain)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sair da Conta", fontWeight = FontWeight.Bold, color = BlackMain, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Diálogo de atualização de contato
    if (showEditContactDialog) {
        var emailInput by remember { mutableStateOf(user?.email ?: "") }
        var phoneInput by remember { mutableStateOf(user?.phone ?: "") }
        var notesInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showEditContactDialog = false },
            containerColor = WhitePure,
            title = {
                Text("Atualização de Contato", fontWeight = FontWeight.Bold, color = BlackMain)
            },
            text = {
                Column {
                    Text(
                        text = "Solicitações de alteração cadastral são registradas para conferência de segurança pela equipe BJSEG.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray600)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Novo E-mail") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlackMain,
                            unfocusedBorderColor = Gray200,
                            cursorColor = BlackMain
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Novo Telefone") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlackMain,
                            unfocusedBorderColor = Gray200,
                            cursorColor = BlackMain
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.requestContactUpdate(emailInput, phoneInput, notesInput)
                        showEditContactDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlackMain,
                        contentColor = WhitePure
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Enviar Solicitação", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditContactDialog = false }) {
                    Text("Cancelar", color = Gray600)
                }
            }
        )
    }

    // Diálogo LGPD
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            containerColor = WhitePure,
            title = {
                Text("Privacidade e LGPD", fontWeight = FontWeight.Bold, color = BlackMain)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "A BJSEG observa com rigor a Lei Geral de Proteção de Dados (LGPD):\n\n" +
                            "• Os dados e ordens de serviço exibidos são restritos à sua conta autenticada.\n" +
                            "• Mascaramento automático do CPF/CNPJ para proteção contra visualização não autorizada.\n" +
                            "• Registro de logs de concessão e auditoria dos créditos acumulados por avaliação.\n" +
                            "• Nenhum dado comercial ou técnico é compartilhado com terceiros sem seu consentimento.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Gray700)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BlackMain, contentColor = WhitePure),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Entendido", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
