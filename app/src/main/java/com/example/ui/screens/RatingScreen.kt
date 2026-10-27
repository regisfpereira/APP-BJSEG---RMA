package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RmaItem
import com.example.ui.BjsegUiState
import com.example.ui.BjsegViewModel
import com.example.ui.theme.BlackMain
import com.example.ui.theme.Gray100
import com.example.ui.theme.Gray200
import com.example.ui.theme.Gray50
import com.example.ui.theme.Gray500
import com.example.ui.theme.Gray600
import com.example.ui.theme.Orange100
import com.example.ui.theme.Orange50
import com.example.ui.theme.Orange500
import com.example.ui.theme.Orange700
import com.example.ui.theme.WhitePure

@Composable
fun RatingScreen(
    rma: RmaItem,
    uiState: BjsegUiState,
    viewModel: BjsegViewModel,
    modifier: Modifier = Modifier
) {
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
        if (uiState.ratingSuccessMessage != null) {
            // Tela de confirmação bem-sucedida
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rating_success_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                border = BorderStroke(1.dp, Gray200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Orange500,
                        modifier = Modifier.size(54.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Avaliação Registrada",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = uiState.ratingSuccessMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Gray600),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = Orange50,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Orange100),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "+ R$ 0,01 em créditos",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Orange700
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Acumulado na sua carteira BJSEG Pós-Vendas",
                                style = MaterialTheme.typography.labelSmall.copy(color = Gray600)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("btn_rating_back_to_rma"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlackMain,
                            contentColor = WhitePure
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Voltar ao Atendimento", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Formulário de Avaliação
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WhitePure),
                border = BorderStroke(1.dp, Gray200),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "AVALIAÇÃO DO ATENDIMENTO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gray500,
                            letterSpacing = 0.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = rma.productName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Gray200, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Grade de informações em 2 colunas
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "NÚMERO DA OS",
                                style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = rma.rmaNumber,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }

                        Box(modifier = Modifier.width(1.dp).height(34.dp).background(Gray200))
                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TIPO",
                                style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = rma.rmaType.displayName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BlackMain
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Faixa de incentivo do programa
                    Surface(
                        color = Orange50,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Orange100),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Orange500,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Avalie seu atendimento e receba R$ 0,01 em créditos para utilizar como desconto em futuras compras na BJSEG.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = BlackMain
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Como foi sua experiência com o Pós-Vendas?",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Linha de 1 a 5 estrelas em Laranja 500 / Cinza 200
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            val isSelected = i <= uiState.ratingStars
                            Icon(
                                imageVector = if (isSelected) Icons.Default.Star else Icons.Outlined.Star,
                                contentDescription = "$i estrelas",
                                tint = if (isSelected) Orange500 else Gray200,
                                modifier = Modifier
                                    .size(44.dp)
                                    .padding(4.dp)
                                    .clickable { viewModel.setRatingStars(i) }
                                    .testTag("star_button_$i")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val starLabel = when (uiState.ratingStars) {
                        1 -> "Muito insatisfeito"
                        2 -> "Insatisfeito"
                        3 -> "Regular"
                        4 -> "Satisfeito"
                        5 -> "Muito satisfeito"
                        else -> ""
                    }
                    Text(
                        text = "${uiState.ratingStars} de 5 estrelas — $starLabel",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Gray500
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Campo de comentário opcional com borda fina em Gray 200
                    OutlinedTextField(
                        value = uiState.ratingComment,
                        onValueChange = { viewModel.setRatingComment(it) },
                        placeholder = {
                            Text(
                                text = "Comentário opcional sobre o atendimento...",
                                style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("rating_comment_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlackMain,
                            unfocusedBorderColor = Gray200,
                            focusedContainerColor = WhitePure,
                            unfocusedContainerColor = WhitePure,
                            cursorColor = BlackMain
                        ),
                        maxLines = 4
                    )

                    if (uiState.ratingErrorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.ratingErrorMessage,
                            style = MaterialTheme.typography.bodySmall.copy(color = Orange700)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botão principal preto
                    Button(
                        onClick = { viewModel.submitRating(rma.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_submit_rating"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlackMain,
                            contentColor = WhitePure
                        ),
                        enabled = !uiState.isSubmittingRating,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (uiState.isSubmittingRating) {
                            CircularProgressIndicator(color = WhitePure, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Enviar Avaliação", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
