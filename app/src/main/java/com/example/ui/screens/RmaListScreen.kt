package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.model.RmaStatus
import com.example.model.RmaType
import com.example.ui.BjsegUiState
import com.example.ui.BjsegViewModel
import com.example.ui.RmaFilterCategory
import com.example.ui.theme.BlackMain
import com.example.ui.theme.Gray100
import com.example.ui.theme.Gray200
import com.example.ui.theme.Gray300
import com.example.ui.theme.Gray400
import com.example.ui.theme.Gray50
import com.example.ui.theme.Gray500
import com.example.ui.theme.Gray600
import com.example.ui.theme.Gray700
import com.example.ui.theme.Orange100
import com.example.ui.theme.Orange200
import com.example.ui.theme.Orange50
import com.example.ui.theme.Orange500
import com.example.ui.theme.Orange600
import com.example.ui.theme.Orange700
import com.example.ui.theme.WhitePure

@Composable
fun RmaListScreen(
    uiState: BjsegUiState,
    viewModel: BjsegViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhitePure)
            .padding(top = 12.dp)
    ) {
        // Campo de busca minimalista com borda fina em Gray 200
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = {
                Text(
                    text = "Buscar por RMA, produto ou código...",
                    style = MaterialTheme.typography.bodySmall.copy(color = Gray500)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Buscar",
                    tint = BlackMain,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpar",
                            tint = Gray500,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("rma_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BlackMain,
                unfocusedBorderColor = Gray200,
                focusedContainerColor = WhitePure,
                unfocusedContainerColor = WhitePure,
                cursorColor = BlackMain
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filtros horizontais com chips limpos
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RmaFilterCategory.entries.forEach { category ->
                val isSelected = uiState.selectedFilter == category
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.onFilterSelected(category) },
                    label = {
                        Text(
                            text = category.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
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
                        selected = isSelected
                    ),
                    modifier = Modifier.testTag("filter_chip_${category.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Contador com badge Laranja 500
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Atendimentos encontrados",
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    color = Orange500,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = uiState.filteredRmas.size.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                    )
                }
            }
        }

        // Lista de cartões com grade de informações de 2 colunas
        if (uiState.filteredRmas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Description,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Gray400
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Nenhum atendimento encontrado para os critérios informados.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Gray500),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.filteredRmas, key = { it.id }) { rma ->
                    RmaMinimalCard(
                        rma = rma,
                        onViewOs = { viewModel.openRmaDetail(rma.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RmaMinimalCard(
    rma: RmaItem,
    onViewOs: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("rma_card_${rma.rmaNumber}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WhitePure),
        border = BorderStroke(1.dp, Gray200),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Topo do card: Número do RMA e Badge de status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rma.rmaNumber,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BlackMain
                    )
                )

                RmaStatusMinimalBadge(status = rma.status)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = rma.productName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = BlackMain
                )
            )

            if (rma.productCode.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Código: ${rma.productCode}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Gray500)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Gray200, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Cartão de grade de 2 colunas
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "TIPO DE ATENDIMENTO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Gray500,
                            letterSpacing = 0.4.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = rma.rmaType.displayName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(Gray200)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ABERTURA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Gray500,
                            letterSpacing = 0.4.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = rma.openedDateFormatted,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BlackMain
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Linha da loja e botão preto de ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Gray600
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = rma.store.name.split("—").first().trim(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = Gray700
                        ),
                        maxLines = 1
                    )
                }

                // Botão principal pequeno, preto, texto branco
                Button(
                    onClick = onViewOs,
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("btn_view_os_${rma.rmaNumber}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlackMain,
                        contentColor = WhitePure
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Ver minha OS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun RmaStatusMinimalBadge(
    status: RmaStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderColor) = when (status) {
        RmaStatus.AGUARDANDO_PAGAMENTO -> Triple(Orange50, Orange700, Orange200)
        RmaStatus.LIBERADO -> Triple(Orange50, Orange600, Orange100)
        RmaStatus.DISPONIVEL_RETIRADA -> Triple(Gray100, BlackMain, Gray300)
        RmaStatus.TROCA -> Triple(Gray100, BlackMain, Gray300)
        else -> Triple(Gray50, Gray700, Gray200)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Text(
            text = status.clientTitle,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
