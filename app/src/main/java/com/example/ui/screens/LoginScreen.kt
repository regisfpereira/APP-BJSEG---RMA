package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
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
import com.example.ui.theme.BjsegGraphite
import com.example.ui.theme.BjsegOrange
import com.example.ui.theme.BjsegOrangeLight

@Composable
fun LoginScreen(
    uiState: BjsegUiState,
    viewModel: BjsegViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo & Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(BjsegGraphite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "BJSEG Pós-Vendas",
                    tint = BjsegOrangeLight,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "BJSEG Pós-Vendas",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BjsegOrange
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Acompanhamento digital de RMAs, manutenção e ordens de serviço",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Auth Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!uiState.isOtpStep) {
                        // Step 1: Informar CPF / CNPJ
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = BjsegOrange,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Acesso Seguro do Cliente",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Informe seu CPF ou CNPJ cadastrado na BJSEG para consultar seus atendimentos.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = uiState.loginDoc,
                            onValueChange = { viewModel.onLoginDocChanged(it) },
                            label = { Text("CPF ou CNPJ") },
                            placeholder = { Text("000.000.000-00 ou 00.000.000/0000-00") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = BjsegOrange)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_document_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BjsegOrange,
                                focusedLabelColor = BjsegOrange
                            )
                        )

                        if (uiState.loginError != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.loginError,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.requestLoginOtp() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_continue_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = BjsegOrange),
                            enabled = !uiState.loginLoading && uiState.loginDoc.isNotBlank(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (uiState.loginLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Avançar para Confirmação", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        // Step 2: Inserir Código Temporário (OTP)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.VpnKey,
                                contentDescription = null,
                                tint = BjsegOrange,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Código de Confirmação",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Enviamos um código temporário de 6 dígitos para o telefone/e-mail cadastrado em ${uiState.loginDoc}.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )

                        // Demo Helper Banner
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = Color(0xFFFEF3C7),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "SMS Simulado (Modo Demonstração):",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                )
                                Text(
                                    text = "Seu código de acesso BJSEG é 123456",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF78350F)
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = uiState.loginOtp,
                            onValueChange = { viewModel.onLoginOtpChanged(it) },
                            label = { Text("Código de 6 dígitos") },
                            placeholder = { Text("123456") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = BjsegOrange)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_otp_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BjsegOrange,
                                focusedLabelColor = BjsegOrange
                            )
                        )

                        if (uiState.loginError != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.loginError,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.verifyLoginOtp() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_verify_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = BjsegOrange),
                            enabled = !uiState.loginLoading && uiState.loginOtp.length == 6,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (uiState.loginLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Confirmar e Entrar", fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { viewModel.onLoginDocChanged(uiState.loginDoc) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Alterar Documento", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Demo Profiles
            Text(
                text = "Preenchimento Rápido (Demonstração):",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    modifier = Modifier.testTag("chip_demo_company")
                )
                Spacer(modifier = Modifier.width(10.dp))
                FilterChip(
                    selected = uiState.loginDoc.startsWith("123.456"),
                    onClick = { viewModel.quickFillDemo(isCompany = false) },
                    label = { Text("Residencial (CPF)") },
                    modifier = Modifier.testTag("chip_demo_residential")
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Security note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Acesso exclusivo aos seus atendimentos autorizados.",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
    }
}
