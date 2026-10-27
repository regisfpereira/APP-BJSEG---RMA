package com.example.model

import java.text.NumberFormat
import java.util.Locale

data class CreditTransaction(
    val id: String,
    val customerId: String,
    val rmaId: String?,
    val rmaNumber: String?,
    val type: String, // "Crédito por avaliação" or "Desconto em compra"
    val amount: Double, // +0.01
    val dateTimeDisplay: String,
    val status: String = "Confirmado",
    val description: String
) {
    val formattedAmount: String
        get() {
            val ptBr = Locale.forLanguageTag("pt-BR")
            val format = NumberFormat.getCurrencyInstance(ptBr)
            val prefix = if (amount > 0) "+" else ""
            return prefix + format.format(amount)
        }
}

data class CreditWallet(
    val customerId: String,
    val balance: Double,
    val minThreshold: Double = 5.00,
    val transactions: List<CreditTransaction>
) {
    val formattedBalance: String
        get() {
            val ptBr = Locale.forLanguageTag("pt-BR")
            return NumberFormat.getCurrencyInstance(ptBr).format(balance)
        }

    val formattedThreshold: String
        get() {
            val ptBr = Locale.forLanguageTag("pt-BR")
            return NumberFormat.getCurrencyInstance(ptBr).format(minThreshold)
        }

    val remainingToThreshold: Double
        get() = maxOf(0.0, minThreshold - balance)

    val formattedRemaining: String
        get() {
            val ptBr = Locale.forLanguageTag("pt-BR")
            return NumberFormat.getCurrencyInstance(ptBr).format(remainingToThreshold)
        }

    val progressFraction: Float
        get() = (balance / minThreshold).coerceIn(0.0, 1.0).toFloat()

    val canUseDiscount: Boolean
        get() = balance >= minThreshold
}
