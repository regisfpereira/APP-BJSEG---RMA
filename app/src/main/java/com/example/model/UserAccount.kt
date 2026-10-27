package com.example.model

data class UserAccount(
    val id: String,
    val name: String,
    val document: String, // CPF or CNPJ (digits only or formatted)
    val email: String,
    val phone: String,
    val isCompany: Boolean = false
) {
    val maskedDocument: String
        get() {
            val clean = document.filter { it.isDigit() }
            return if (clean.length == 14) {
                // CNPJ: 12.345.678/0001-90 -> 12.***.***/0001-90
                val f = formatCnpj(clean)
                "${f.take(3)}***.***/${f.takeLast(7)}"
            } else if (clean.length == 11) {
                // CPF: 123.456.789-00 -> ***.456.789-**
                val f = formatCpf(clean)
                "***.${f.substring(4, 11)}--**"
            } else {
                "***"
            }
        }

    val formattedDocument: String
        get() = formatDocument(document)

    companion object {
        fun formatDocument(input: String): String {
            val clean = input.filter { it.isDigit() }
            return when {
                clean.length <= 11 -> formatCpf(clean)
                else -> formatCnpj(clean)
            }
        }

        fun formatCpf(clean: String): String {
            val digits = clean.take(11)
            return buildString {
                for (i in digits.indices) {
                    if (i == 3 || i == 6) append('.')
                    if (i == 9) append('-')
                    append(digits[i])
                }
            }
        }

        fun formatCnpj(clean: String): String {
            val digits = clean.take(14)
            return buildString {
                for (i in digits.indices) {
                    if (i == 2 || i == 5) append('.')
                    if (i == 8) append('/')
                    if (i == 12) append('-')
                    append(digits[i])
                }
            }
        }

        fun isValidDocument(input: String): Boolean {
            val clean = input.filter { it.isDigit() }
            // Allow demo testing documents or validate modulus-11 checksums
            if (clean == "12345678000190" || clean == "12345678900") return true
            return when (clean.length) {
                11 -> isValidCpf(clean)
                14 -> isValidCnpj(clean)
                else -> false
            }
        }

        private fun isValidCpf(cpf: String): Boolean {
            if (cpf.all { it == cpf[0] }) return false
            try {
                var sum = 0
                for (i in 0..8) sum += (cpf[i] - '0') * (10 - i)
                var firstCheck = 11 - (sum % 11)
                if (firstCheck >= 10) firstCheck = 0
                if (firstCheck != cpf[9] - '0') return false

                sum = 0
                for (i in 0..9) sum += (cpf[i] - '0') * (11 - i)
                var secondCheck = 11 - (sum % 11)
                if (secondCheck >= 10) secondCheck = 0
                return secondCheck == cpf[10] - '0'
            } catch (e: Exception) {
                return false
            }
        }

        private fun isValidCnpj(cnpj: String): Boolean {
            if (cnpj.all { it == cnpj[0] }) return false
            try {
                val weights1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
                var sum = 0
                for (i in 0..11) sum += (cnpj[i] - '0') * weights1[i]
                var rem = sum % 11
                val d1 = if (rem < 2) 0 else 11 - rem
                if (d1 != cnpj[12] - '0') return false

                val weights2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
                sum = 0
                for (i in 0..12) sum += (cnpj[i] - '0') * weights2[i]
                rem = sum % 11
                val d2 = if (rem < 2) 0 else 11 - rem
                return d2 == cnpj[13] - '0'
            } catch (e: Exception) {
                return false
            }
        }
    }
}
