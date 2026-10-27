package com.example.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class RmaStatus(
    val internalCode: String,
    val clientTitle: String,
    val explanation: String,
    val generatesPush: Boolean,
    val isFinished: Boolean = false
) {
    NOVO(
        internalCode = "NOVO",
        clientTitle = "RMA aberto",
        explanation = "Seu atendimento foi registrado e está aguardando as próximas etapas do processo.",
        generatesPush = false
    ),
    CONFIRMADO(
        internalCode = "CONFIRMADO",
        clientTitle = "Em análise pela BJSEG",
        explanation = "Seu produto chegou ao setor de Pós-Vendas e está passando pelas etapas de triagem e encaminhamento.",
        generatesPush = false
    ),
    ENVIADO_AO_FORNECEDOR(
        internalCode = "ENVIADO_AO_FORNECEDOR",
        clientTitle = "Em análise pela assistência",
        explanation = "Seu produto foi encaminhado para avaliação ou manutenção pela assistência técnica responsável.",
        generatesPush = false
    ),
    AGUARDANDO_PAGAMENTO(
        internalCode = "AGUARDANDO_PAGAMENTO",
        clientTitle = "Aguardando pagamento",
        explanation = "Há uma etapa de pagamento pendente para dar continuidade ao atendimento. Entre em contato com o Pós-Vendas para receber as orientações.",
        generatesPush = true
    ),
    LIBERADO(
        internalCode = "LIBERADO",
        clientTitle = "Retornando à loja",
        explanation = "Seu produto foi liberado para retorno à loja de origem. Aguarde a confirmação de que está disponível para retirada antes de se deslocar até a unidade.",
        generatesPush = true
    ),
    DISPONIVEL_RETIRADA(
        internalCode = "DISPONIVEL_RETIRADA",
        clientTitle = "Disponível para retirada",
        explanation = "Seu produto chegou à loja e está disponível para retirada. Consulte o endereço e retire o produto na unidade.",
        generatesPush = true,
        isFinished = true
    ),
    TROCA(
        internalCode = "TROCA",
        clientTitle = "Troca efetuada",
        explanation = "A troca do produto foi registrada como concluída. Obrigado por utilizar os serviços da BJSEG.",
        generatesPush = true,
        isFinished = true
    )
}

enum class RmaType(val displayName: String, val detailedDescription: String) {
    MANUTENCAO_GARANTIA(
        displayName = "Manutenção em garantia",
        detailedDescription = "Produto encaminhado para avaliação ou manutenção no âmbito da garantia, sujeito às condições e à análise técnica aplicáveis."
    ),
    MANUTENCAO_CLIENTE(
        displayName = "Manutenção cliente",
        detailedDescription = "Manutenção fora da garantia ou em que os custos devem ser assumidos pelo cliente."
    ),
    TROCA(
        displayName = "Troca",
        detailedDescription = "Atendimento destinado à substituição do produto."
    )
}

data class RmaTimelineEvent(
    val status: RmaStatus,
    val title: String,
    val dateDisplay: String,
    val explanation: String,
    val additionalInfo: String? = null
)

data class RmaItem(
    val id: String,
    val rmaNumber: String,
    val customerId: String,
    val customerName: String,
    val productCode: String,
    val productName: String,
    val serialNumber: String? = null,
    val rmaType: RmaType,
    val status: RmaStatus,
    val storeId: String,
    val openedDate: String, // e.g. "2026-10-10"
    val customerDefect: String,
    val customerNotes: String? = null,
    val timeline: List<RmaTimelineEvent>,
    val isRated: Boolean = false,
    val ratingStars: Int? = null,
    val ratingComment: String? = null
) {
    val store: BjsegStore
        get() = BjsegStore.findById(storeId)

    /**
     * Estimated deadline: Opening date + 30 consecutive calendar days.
     * Prompt specifies: "calcular a previsão exibida no aplicativo a partir da data de abertura do RMA,
     * acrescentando 30 dias corridos."
     */
    val estimatedDeadlineDisplay: String
        get() {
            return try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outFormat = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))
                val date = sdf.parse(openedDate) ?: return "30 dias após a abertura"
                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.DAY_OF_YEAR, 30)
                outFormat.format(cal.time)
            } catch (e: Exception) {
                "30 dias após a abertura"
            }
        }

    val openedDateFormatted: String
        get() {
            return try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outFormat = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))
                val date = sdf.parse(openedDate) ?: return openedDate
                outFormat.format(date)
            } catch (e: Exception) {
                openedDate
            }
        }

    val isPendingPayment: Boolean
        get() = status == RmaStatus.AGUARDANDO_PAGAMENTO

    val isReadyForPickup: Boolean
        get() = status == RmaStatus.DISPONIVEL_RETIRADA

    val isEligibleForRating: Boolean
        get() = (status == RmaStatus.DISPONIVEL_RETIRADA || status == RmaStatus.TROCA) && !isRated
}
