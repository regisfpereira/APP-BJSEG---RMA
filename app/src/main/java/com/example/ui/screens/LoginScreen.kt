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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BjsegUiState
import com.example.ui.BjsegViewModel
import com.example.ui.theme.BlackMain
import com.example.ui.theme.Gray100
import com.example.ui.theme.Gray200
import com.example.ui.theme.Gray50
import com.example.ui.theme.Gray500
import com.example.ui.theme.Gray700
import com.example.ui.theme.Orange100
import com.example.ui.theme.Orange50
import com.example.ui.theme.Orange500
import com.example.ui.theme.Orange700
import com.example.ui.theme.WhitePure

@Composable
fun LoginScreen(
    uiState: BjsegUiState,
    viewModel: BjsegViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = WhitePure
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo circular com borda fina em Gray 200
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(BorderStroke(1.dp, Gray200), CircleShape)
                    .background(Gray50),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Security,
                    contentDescription = "BJSEG Pós-Vendas",
                    tint = BlackMain,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "BJSEG Pós-Vendas",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BlackMain
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Acompanhamento digital de ordens de serviço, manutenção e créditos",
                style = MaterialTheme.typography.bodySmall.copy(color = Gray500),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Card principal de autenticação com borda fina
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
                    if (!uiState.isOtpStep) {
                        // Etapa 1: CPF / CNPJ
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Acesso Seguro do Cliente",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Informe seu CPF ou CNPJ cadastrado na BJSEG para consultar suas ordens de serviço autorizadas.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = uiState.loginDoc,
                            onValueChange = { viewModel.onLoginDocChanged(it) },
                            placeholder = {
                                Text(
                                    text = "000.000.000-00 ou 00.000.000/0000-00",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = BlackMain,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_document_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BlackMain,
                                unfocusedBorderColor = Gray200,
                                focusedContainerColor = WhitePure,
                                unfocusedContainerColor = WhitePure,
                                cursorColor = BlackMain
                            )
                        )

                        if (uiState.loginError != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.loginError,
                                style = MaterialTheme.typography.bodySmall.copy(color = Orange700),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Botão principal pequeno, preto, texto branco
                        Button(
                            onClick = { viewModel.requestLoginOtp() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("login_continue_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BlackMain,
                                contentColor = WhitePure
                            ),
                            enabled = !uiState.loginLoading && uiState.loginDoc.isNotBlank(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (uiState.loginLoading) {
                                CircularProgressIndicator(color = WhitePure, modifier = Modifier.size(20.dp))
                            } else {
                                Text("Avançar para Confirmação", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        // Etapa 2: Código OTP
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Código de Confirmação",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Enviamos um código temporário de 6 dígitos para o contato cadastrado em ${uiState.loginDoc}.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Faixa de SMS em modo demonstração
                        Surface(
                            color = Orange50,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Orange100),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "SMS Simulado (Modo Demonstração):",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Orange700
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Seu código de acesso temporário BJSEG é 123456",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = BlackMain
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = uiState.loginOtp,
                            onValueChange = { viewModel.onLoginOtpChanged(it) },
                            placeholder = {
                                Text(
                                    text = "123456",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.VpnKey,
                                    contentDescription = null,
                                    tint = BlackMain,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_otp_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BlackMain,
                                unfocusedBorderColor = Gray200,
                                focusedContainerColor = WhitePure,
                                unfocusedContainerColor = WhitePure,
                                cursorColor = BlackMain
                            )
                        )

                        if (uiState.loginError != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.loginError,
                                style = MaterialTheme.typography.bodySmall.copy(color = Orange700),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.verifyLoginOtp() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("login_verify_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BlackMain,
                                contentColor = WhitePure
                            ),
                            enabled = !uiState.loginLoading && uiState.loginOtp.length == 6,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (uiState.loginLoading) {
                                CircularProgressIndicator(color = WhitePure, modifier = Modifier.size(20.dp))
                            } else {
                                Text("Confirmar e Entrar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { viewModel.onLoginDocChanged(uiState.loginDoc) },
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Gray200)
                        ) {
                            Text("Alterar Documento", color = BlackMain, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Chips de preenchimento rápido em modo de teste
            Text(
                text = "Preenchimento Rápido (Demonstração):",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Gray500
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = uiState.loginDoc.startsWith("12.345"),
                    onClick = { viewModel.quickFillDemo(isCompany = true) },
                    label = { Text("Empresa (CNPJ)") },
                    shape = RoundedCornerShape(6.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BlackMain,
                        selectedLabelColor = WhitePure,
                        containerColor = Gray100,
                        labelColor = Gray700
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Gray200,
                        selectedBorderColor = BlackMain,
                        borderWidth = 1.dp,
                        enabled = true,
                        selected = uiState.loginDoc.startsWith("12.345")
                    ),
                    modifier = Modifier.testTag("chip_demo_company")
                )

                Spacer(modifier = Modifier.width(10.dp))

                FilterChip(
                    selected = uiState.loginDoc.startsWith("123.456"),
                    onClick = { viewModel.quickFillDemo(isCompany = false) },
                    label = { Text("Residencial (CPF)") },
                    shape = RoundedCornerShape(6.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BlackMain,
                        selectedLabelColor = WhitePure,
                        containerColor = Gray100,
                        labelColor = Gray700
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Gray200,
                        selectedBorderColor = BlackMain,
                        borderWidth = 1.dp,
                        enabled = true,
                        selected = uiState.loginDoc.startsWith("123.456")
                    ),
                    modifier = Modifier.testTag("chip_demo_residential")
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Orange500)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Acesso exclusivo aos registros vinculados ao seu cadastro oficial.",
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                )
            }
        }
    }
}
