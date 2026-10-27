package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.ui.theme.BjsegGold
import com.example.ui.theme.BjsegOrange

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
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        if (uiState.ratingSuccessMessage != null) {
            // Success Screen
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rating_success_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Avaliação Registrada!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534)
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = uiState.ratingSuccessMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(10.dp),
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
                                    color = Color(0xFF166534)
                                )
                            )
                            Text(
                                text = "Acumulado na sua carteira BJSEG Pós-Vendas",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF15803D))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_rating_back_to_rma"),
                        colors = ButtonDefaults.buttonColors(containerColor = BjsegOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Voltar ao Atendimento", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Rating Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Header with RMA info
                    Text(
                        text = "AVALIAÇÃO DO ATENDIMENTO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BjsegOrange,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = rma.rmaNumber,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = rma.productName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    RmaTypeBadge(type = rma.rmaType)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Program incentive banner
                    Surface(
                        color = Color(0xFFFFFBEB),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Avalie seu atendimento e receba R$ 0,01 em créditos para utilizar como desconto em futuras compras na BJSEG.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF92400E)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Question
                    Text(
                        text = "Como foi sua experiência com o atendimento de Pós-Vendas da BJSEG?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 1 to 5 Stars Row
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
                                tint = if (isSelected) BjsegGold else Color(0xFFCBD5E1),
                                modifier = Modifier
                                    .size(46.dp)
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
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Optional Comment Field
                    OutlinedTextField(
                        value = uiState.ratingComment,
                        onValueChange = { viewModel.setRatingComment(it) },
                        label = { Text("Comentário (opcional)") },
                        placeholder = { Text("Conte com mais detalhes como foi o atendimento do RMA...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .testTag("rating_comment_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BjsegOrange,
                            focusedLabelColor = BjsegOrange
                        ),
                        maxLines = 4
                    )

                    if (uiState.ratingErrorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.ratingErrorMessage,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.submitRating(rma.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_submit_rating"),
                        colors = ButtonDefaults.buttonColors(containerColor = BjsegOrange),
                        enabled = !uiState.isSubmittingRating,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (uiState.isSubmittingRating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                        } else {
                            Text("Enviar Avaliação", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
