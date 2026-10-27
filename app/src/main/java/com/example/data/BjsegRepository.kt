package com.example.data

import com.example.model.BjsegStore
import com.example.model.CreditTransaction
import com.example.model.CreditWallet
import com.example.model.NotificationItem
import com.example.model.RmaItem
import com.example.model.RmaStatus
import com.example.model.RmaTimelineEvent
import com.example.model.RmaType
import com.example.model.UserAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class BjsegRepository private constructor() {

    // Configurable official WhatsApp support contact
    private val _whatsAppSupportNumber = MutableStateFlow("551146467000") // Central BJSEG Pós-Vendas
    val whatsAppSupportNumber: Flow<String> = _whatsAppSupportNumber.asStateFlow()

    // Current authenticated session
    private val _currentUser = MutableStateFlow<UserAccount?>(DEMO_COMPANY_USER)
    val currentUser: Flow<UserAccount?> = _currentUser.asStateFlow()

    // In-memory persistent database for RMA items (with Object Data API simulation layer)
    private val _rmas = MutableStateFlow<List<RmaItem>>(INITIAL_RMAS)
    val rmas: Flow<List<RmaItem>> = _rmas.asStateFlow()

    // In-memory credit transactions and balances
    private val _transactions = MutableStateFlow<List<CreditTransaction>>(INITIAL_TRANSACTIONS)

    // Notification center state
    private val _notifications = MutableStateFlow<List<NotificationItem>>(INITIAL_NOTIFICATIONS)
    val notifications: Flow<List<NotificationItem>> = _notifications.asStateFlow()

    // Demo mode flag (clearly identifying mock data until real API is wired)
    private val _isDemoMode = MutableStateFlow(true)
    val isDemoMode: Flow<Boolean> = _isDemoMode.asStateFlow()

    // Temporary OTP storage for verification flow
    private val _pendingOtp = MutableStateFlow<String?>("123456")
    val pendingOtp: Flow<String?> = _pendingOtp.asStateFlow()

    fun updateWhatsAppNumber(newNumber: String) {
        val clean = newNumber.filter { it.isDigit() }
        if (clean.isNotBlank()) {
            _whatsAppSupportNumber.value = clean
        }
    }

    fun buildWhatsAppUrl(rmaNumber: String, isPayment: Boolean): String {
        val number = _whatsAppSupportNumber.value
        val message = if (isPayment) {
            "Olá! Gostaria de realizar o pagamento referente ao RMA $rmaNumber. Poderiam me orientar sobre o pagamento, por favor?"
        } else {
            "Olá! Gostaria de obter informações sobre o RMA $rmaNumber. Poderiam me ajudar, por favor?"
        }
        val encodedMsg = URLEncoder.encode(message, "UTF-8")
        return "https://wa.me/$number?text=$encodedMsg"
    }

    fun buildGeneralWhatsAppUrl(): String {
        val number = _whatsAppSupportNumber.value
        val message = "Olá! Gostaria de falar com o setor de Pós-Vendas da BJSEG. Poderiam me atender, por favor?"
        val encodedMsg = URLEncoder.encode(message, "UTF-8")
        return "https://wa.me/$number?text=$encodedMsg"
    }

    /**
     * Authenticate by CPF or CNPJ.
     * Generates a temporary 6-digit OTP code sent to customer phone/email.
     */
    fun requestLogin(rawDoc: String): Result<String> {
        val clean = rawDoc.filter { it.isDigit() }
        if (!UserAccount.isValidDocument(clean)) {
            return Result.failure(IllegalArgumentException("Documento inválido. Informe um CPF (11 dígitos) ou CNPJ (14 dígitos) válido."))
        }

        // Demo logic: matching or creating account
        val code = "123456" // Standard demo OTP code
        _pendingOtp.value = code
        return Result.success(code)
    }

    fun verifyLoginOtp(rawDoc: String, code: String): Result<UserAccount> {
        val clean = rawDoc.filter { it.isDigit() }
        if (code != "123456" && code != _pendingOtp.value) {
            return Result.failure(IllegalArgumentException("Código de verificação incorreto ou expirado. Tente novamente."))
        }

        // Check if demo user or new session
        val user = when {
            clean.length == 14 -> DEMO_COMPANY_USER.copy(document = clean)
            clean.length == 11 && clean.startsWith("123") -> DEMO_RESIDENTIAL_USER
            else -> UserAccount(
                id = "usr_${clean.take(6)}",
                name = if (clean.length == 14) "Segurança Eletrônica São Paulo Ltda" else "Cliente BJSEG",
                document = clean,
                email = "cliente@bjseg-posvendas.com.br",
                phone = "(11) 98765-4321",
                isCompany = clean.length == 14
            )
        }

        _currentUser.value = user
        _pendingOtp.value = null
        return Result.success(user)
    }

    fun logout() {
        _currentUser.value = null
    }

    fun switchDemoUser(company: Boolean) {
        _currentUser.value = if (company) DEMO_COMPANY_USER else DEMO_RESIDENTIAL_USER
    }

    fun getCreditWallet(): Flow<CreditWallet> {
        return _transactions.map { list ->
            val userId = _currentUser.value?.id ?: ""
            val userTx = list.filter { it.customerId == userId }
            val balance = userTx.sumOf { it.amount }
            CreditWallet(
                customerId = userId,
                balance = balance,
                minThreshold = 5.00,
                transactions = userTx.sortedByDescending { it.dateTimeDisplay }
            )
        }
    }

    /**
     * Server-side evaluation submission:
     * - Validates eligibility (RMA must be finished / exchange done).
     * - Anti-duplication check: ensures this RMA has not been evaluated already.
     * - Awards exactly R$ 0,01 regardless of score (1 to 5 stars).
     * - Records transaction and updates RMA state.
     */
    fun submitRmaRating(rmaId: String, stars: Int, comment: String?): Result<Double> {
        val currentList = _rmas.value
        val rma = currentList.firstOrNull { it.id == rmaId }
            ?: return Result.failure(IllegalStateException("RMA não encontrado."))

        if (rma.isRated) {
            return Result.failure(IllegalStateException("Você já avaliou este atendimento anteriormente."))
        }

        if (rma.status != RmaStatus.DISPONIVEL_RETIRADA && rma.status != RmaStatus.TROCA) {
            return Result.failure(IllegalStateException("A avaliação só está disponível após a conclusão do atendimento ou troca."))
        }

        val clampedStars = stars.coerceIn(1, 5)
        val updatedRma = rma.copy(
            isRated = true,
            ratingStars = clampedStars,
            ratingComment = comment?.trim()?.ifBlank { null }
        )

        // Update RMA
        _rmas.value = currentList.map { if (it.id == rmaId) updatedRma else it }

        // Record R$ 0.01 credit transaction
        val awardAmount = 0.01
        val nowFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("pt-BR"))
        val newTx = CreditTransaction(
            id = "tx_${UUID.randomUUID().toString().take(8)}",
            customerId = rma.customerId,
            rmaId = rma.id,
            rmaNumber = rma.rmaNumber,
            type = "Crédito por avaliação",
            amount = awardAmount,
            dateTimeDisplay = nowFormat.format(Date()),
            status = "Confirmado",
            description = "Avaliação ($clampedStars★) do atendimento ${rma.rmaNumber} - ${rma.productName}"
        )

        _transactions.value = listOf(newTx) + _transactions.value
        return Result.success(awardAmount)
    }

    fun markNotificationAsRead(notificationId: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun requestContactUpdate(email: String, phone: String, notes: String): Result<String> {
        val current = _currentUser.value ?: return Result.failure(IllegalStateException("Usuário não autenticado"))
        _currentUser.value = current.copy(
            email = email.ifBlank { current.email },
            phone = phone.ifBlank { current.phone }
        )
        return Result.success("Sua solicitação de atualização cadastral foi enviada com sucesso para o setor de atendimento da BJSEG.")
    }

    companion object {
        @Volatile
        private var INSTANCE: BjsegRepository? = null

        fun getInstance(): BjsegRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BjsegRepository().also { INSTANCE = it }
            }
        }

        val DEMO_COMPANY_USER = UserAccount(
            id = "usr_segurtech",
            name = "SegurTech Soluções e Monitoramento Ltda",
            document = "12345678000190",
            email = "posvendas@segurtech.com.br",
            phone = "(11) 4646-1234",
            isCompany = true
        )

        val DEMO_RESIDENTIAL_USER = UserAccount(
            id = "usr_joao",
            name = "João da Silva Pereira",
            document = "12345678900",
            email = "joao.pereira@gmail.com",
            phone = "(12) 99123-4567",
            isCompany = false
        )

        val INITIAL_RMAS = listOf(
            RmaItem(
                id = "rma_01",
                rmaNumber = "RMA-2026-0842",
                customerId = "usr_segurtech",
                customerName = "SegurTech Soluções e Monitoramento Ltda",
                productCode = "CAM-IP-4K-PRO",
                productName = "Câmera Bullet IP 4K 8MP IR 40m com IA",
                serialNumber = "BJ-2026-X99281A",
                rmaType = RmaType.MANUTENCAO_CLIENTE,
                status = RmaStatus.AGUARDANDO_PAGAMENTO,
                storeId = "itaqua",
                openedDate = "2026-10-18",
                customerDefect = "Equipamento sofreu descarga atmosférica. Conector RJ45 sem comunicação e LED PoE não acende.",
                customerNotes = "Laudo técnico emitido. Manutenção fora da garantia (danos elétricos). Aguardando autorização e pagamento tabelado.",
                timeline = listOf(
                    RmaTimelineEvent(
                        status = RmaStatus.AGUARDANDO_PAGAMENTO,
                        title = "Aguardando pagamento",
                        dateDisplay = "26/10/2026 14:15",
                        explanation = "Há uma etapa de pagamento pendente para dar continuidade ao atendimento. Entre em contato com o Pós-Vendas para receber as orientações.",
                        additionalInfo = "Utilize o botão 'Pagar agora' para obter as instruções pelo WhatsApp oficial."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.ENVIADO_AO_FORNECEDOR,
                        title = "Em análise pela assistência",
                        dateDisplay = "21/10/2026 10:30",
                        explanation = "Seu produto foi encaminhado para avaliação ou manutenção pela assistência técnica responsável."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.CONFIRMADO,
                        title = "Em análise pela BJSEG",
                        dateDisplay = "19/10/2026 09:12",
                        explanation = "Seu produto chegou ao setor de Pós-Vendas e está passando pelas etapas de triagem e encaminhamento."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.NOVO,
                        title = "RMA aberto",
                        dateDisplay = "18/10/2026 16:40",
                        explanation = "Seu atendimento foi registrado e está aguardando as próximas etapas do processo."
                    )
                )
            ),
            RmaItem(
                id = "rma_02",
                rmaNumber = "RMA-2026-0795",
                customerId = "usr_segurtech",
                customerName = "SegurTech Soluções e Monitoramento Ltda",
                productCode = "NVR-32CH-4K",
                productName = "Gravador Digital NVR 32 Canais 4K H.265+",
                serialNumber = "BJ-2025-NVR8812",
                rmaType = RmaType.MANUTENCAO_GARANTIA,
                status = RmaStatus.DISPONIVEL_RETIRADA,
                storeId = "sjc_loja",
                openedDate = "2026-09-28",
                customerDefect = "Não reconhece portas SATA 3 e 4 após 6 meses de uso contínuo.",
                customerNotes = "Placa mãe substituída em garantia pelo fabricante. Testes de 48h de gravação concluídos com sucesso. Produto pronto na loja.",
                timeline = listOf(
                    RmaTimelineEvent(
                        status = RmaStatus.DISPONIVEL_RETIRADA,
                        title = "Disponível para retirada",
                        dateDisplay = "25/10/2026 11:20",
                        explanation = "Seu produto chegou à loja e está disponível para retirada. Compareça à unidade de São José dos Campos.",
                        additionalInfo = "Apresente o número do RMA e documento com foto para retirada."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.LIBERADO,
                        title = "Retornando à loja",
                        dateDisplay = "23/10/2026 16:45",
                        explanation = "Seu produto foi liberado para retorno à loja de origem."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.ENVIADO_AO_FORNECEDOR,
                        title = "Em análise pela assistência",
                        dateDisplay = "03/10/2026 14:00",
                        explanation = "Seu produto foi encaminhado para avaliação ou manutenção pela assistência técnica responsável."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.CONFIRMADO,
                        title = "Em análise pela BJSEG",
                        dateDisplay = "29/09/2026 10:15",
                        explanation = "Seu produto chegou ao setor de Pós-Vendas e está passando pelas etapas de triagem."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.NOVO,
                        title = "RMA aberto",
                        dateDisplay = "28/09/2026 14:30",
                        explanation = "Seu atendimento foi registrado e está aguardando as próximas etapas do processo."
                    )
                ),
                isRated = false
            ),
            RmaItem(
                id = "rma_03",
                rmaNumber = "RMA-2026-0810",
                customerId = "usr_segurtech",
                customerName = "SegurTech Soluções e Monitoramento Ltda",
                productCode = "SW-POE-16G",
                productName = "Switch Gerenciável 16 Portas Gigabit PoE+ 250W",
                serialNumber = "BJ-2026-SW7110B",
                rmaType = RmaType.TROCA,
                status = RmaStatus.TROCA,
                storeId = "aruja",
                openedDate = "2026-10-05",
                customerDefect = "Portas 9 a 16 perderam alimentação PoE no primeiro dia de instalação.",
                customerNotes = "Defeito de fabricação constatado no prazo de troca rápida. Substituição imediata realizada por equipamento novo lacrado.",
                timeline = listOf(
                    RmaTimelineEvent(
                        status = RmaStatus.TROCA,
                        title = "Troca efetuada",
                        dateDisplay = "08/10/2026 15:30",
                        explanation = "A troca do produto foi registrada como concluída. Obrigado por utilizar os serviços da BJSEG.",
                        additionalInfo = "Novo número de série emitido na nota fiscal de troca: BJ-2026-SW9944C."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.CONFIRMADO,
                        title = "Em análise pela BJSEG",
                        dateDisplay = "06/10/2026 09:00",
                        explanation = "Seu produto chegou ao setor de Pós-Vendas e passou pela triagem."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.NOVO,
                        title = "RMA aberto",
                        dateDisplay = "05/10/2026 11:20",
                        explanation = "Seu atendimento foi registrado e está aguardando as próximas etapas do processo."
                    )
                ),
                isRated = true,
                ratingStars = 5,
                ratingComment = "Troca super rápida na loja de Arujá! Atendimento exemplar do Pós-Vendas."
            ),
            RmaItem(
                id = "rma_04",
                rmaNumber = "RMA-2026-0855",
                customerId = "usr_segurtech",
                customerName = "SegurTech Soluções e Monitoramento Ltda",
                productCode = "CTRL-BIOM-FACE",
                productName = "Controlador de Acesso com Reconhecimento Facial e RFID",
                serialNumber = "BJ-2026-FAC209",
                rmaType = RmaType.MANUTENCAO_GARANTIA,
                status = RmaStatus.LIBERADO,
                storeId = "santos",
                openedDate = "2026-10-20",
                customerDefect = "Tela touch screen travada no logotipo inicial após atualização de firmware.",
                customerNotes = "Firmware recuperado pela assistência técnica em laboratório especializado. Em rota de retorno para a unidade Santos.",
                timeline = listOf(
                    RmaTimelineEvent(
                        status = RmaStatus.LIBERADO,
                        title = "Retornando à loja",
                        dateDisplay = "26/10/2026 17:00",
                        explanation = "Seu produto foi liberado para retorno à loja de origem. Aguarde a confirmação de que está disponível para retirada antes de se deslocar até a unidade.",
                        additionalInfo = "Previsão de chegada na unidade Santos em até 2 dias úteis."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.ENVIADO_AO_FORNECEDOR,
                        title = "Em análise pela assistência",
                        dateDisplay = "22/10/2026 11:00",
                        explanation = "Seu produto foi encaminhado para avaliação ou manutenção pela assistência técnica responsável."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.CONFIRMADO,
                        title = "Em análise pela BJSEG",
                        dateDisplay = "21/10/2026 08:45",
                        explanation = "Seu produto chegou ao setor de Pós-Vendas."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.NOVO,
                        title = "RMA aberto",
                        dateDisplay = "20/10/2026 14:10",
                        explanation = "Seu atendimento foi registrado."
                    )
                )
            ),
            RmaItem(
                id = "rma_05",
                rmaNumber = "RMA-2026-0862",
                customerId = "usr_segurtech",
                customerName = "SegurTech Soluções e Monitoramento Ltda",
                productCode = "DVR-16CH-MHD",
                productName = "DVR Stand Alone Multi-HD 16 Canais 5MP",
                serialNumber = "BJ-2026-DVR3301",
                rmaType = RmaType.MANUTENCAO_GARANTIA,
                status = RmaStatus.CONFIRMADO,
                storeId = "taubate",
                openedDate = "2026-10-25",
                customerDefect = "Perda intermitente de sinal de vídeo nos canais 5 e 6.",
                customerNotes = "Produto recebido na unidade Taubaté e em fase de triagem pela equipe técnica.",
                timeline = listOf(
                    RmaTimelineEvent(
                        status = RmaStatus.CONFIRMADO,
                        title = "Em análise pela BJSEG",
                        dateDisplay = "26/10/2026 10:00",
                        explanation = "Seu produto chegou ao setor de Pós-Vendas e está passando pelas etapas de triagem e encaminhamento."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.NOVO,
                        title = "RMA aberto",
                        dateDisplay = "25/10/2026 16:30",
                        explanation = "Seu atendimento foi registrado e está aguardando as próximas etapas do processo."
                    )
                )
            ),
            // Residential user RMAs
            RmaItem(
                id = "rma_06",
                rmaNumber = "RMA-2026-0870",
                customerId = "usr_joao",
                customerName = "João da Silva Pereira",
                productCode = "KIT-ALARME-SEM-FIO",
                productName = "Central de Alarme Residencial Inteligente com Wi-Fi e 4G",
                serialNumber = "BJ-2026-ALM554",
                rmaType = RmaType.MANUTENCAO_GARANTIA,
                status = RmaStatus.ENVIADO_AO_FORNECEDOR,
                storeId = "sjc_loja",
                openedDate = "2026-10-22",
                customerDefect = "Sensor de presença da zona 2 dispara falso alarme constantemente.",
                customerNotes = "Encaminhado para substituição do sensor de presença em garantia.",
                timeline = listOf(
                    RmaTimelineEvent(
                        status = RmaStatus.ENVIADO_AO_FORNECEDOR,
                        title = "Em análise pela assistência",
                        dateDisplay = "24/10/2026 14:00",
                        explanation = "Seu produto foi encaminhado para avaliação ou manutenção pela assistência técnica responsável."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.CONFIRMADO,
                        title = "Em análise pela BJSEG",
                        dateDisplay = "23/10/2026 09:30",
                        explanation = "Seu produto chegou ao setor de Pós-Vendas."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.NOVO,
                        title = "RMA aberto",
                        dateDisplay = "22/10/2026 15:20",
                        explanation = "Seu atendimento foi registrado."
                    )
                )
            ),
            RmaItem(
                id = "rma_07",
                rmaNumber = "RMA-2026-0760",
                customerId = "usr_joao",
                customerName = "João da Silva Pereira",
                productCode = "VID-PORTEIRO-WIFI",
                productName = "Vídeo Porteiro Wi-Fi com Campainha e Tela LCD 7\"",
                serialNumber = "BJ-2025-VP1120",
                rmaType = RmaType.TROCA,
                status = RmaStatus.TROCA,
                storeId = "sjc_loja",
                openedDate = "2026-09-15",
                customerDefect = "Alto-falante do módulo externo com chiado agudo.",
                customerNotes = "Troca imediata por novo produto lacrado autorizada pela gerência.",
                timeline = listOf(
                    RmaTimelineEvent(
                        status = RmaStatus.TROCA,
                        title = "Troca efetuada",
                        dateDisplay = "18/09/2026 16:00",
                        explanation = "A troca do produto foi registrada como concluída. Obrigado por utilizar os serviços da BJSEG."
                    ),
                    RmaTimelineEvent(
                        status = RmaStatus.NOVO,
                        title = "RMA aberto",
                        dateDisplay = "15/09/2026 10:00",
                        explanation = "Seu atendimento foi registrado."
                    )
                ),
                isRated = false
            )
        )

        val INITIAL_TRANSACTIONS = listOf(
            CreditTransaction(
                id = "tx_01",
                customerId = "usr_segurtech",
                rmaId = "rma_03",
                rmaNumber = "RMA-2026-0810",
                type = "Crédito por avaliação",
                amount = 0.01,
                dateTimeDisplay = "08/10/2026 16:05",
                status = "Confirmado",
                description = "Avaliação (5★) do atendimento RMA-2026-0810 - Switch Gerenciável 16 Portas Gigabit PoE+ 250W"
            ),
            // Prior accumulation demo transactions to show realistic wallet
            CreditTransaction(
                id = "tx_02",
                customerId = "usr_segurtech",
                rmaId = null,
                rmaNumber = "RMA-2026-0612",
                type = "Crédito por avaliação",
                amount = 0.01,
                dateTimeDisplay = "15/08/2026 11:20",
                status = "Confirmado",
                description = "Avaliação (5★) do atendimento RMA-2026-0612 - Câmera Dome 2MP"
            ),
            CreditTransaction(
                id = "tx_03",
                customerId = "usr_segurtech",
                rmaId = null,
                rmaNumber = "RMA-2026-0540",
                type = "Crédito por avaliação",
                amount = 0.01,
                dateTimeDisplay = "02/07/2026 14:45",
                status = "Confirmado",
                description = "Avaliação (4★) do atendimento RMA-2026-0540 - Fechadura Eletroímã 300kg"
            ),
            CreditTransaction(
                id = "tx_04",
                customerId = "usr_segurtech",
                rmaId = null,
                rmaNumber = "RMA-2026-0490",
                type = "Crédito por avaliação",
                amount = 0.01,
                dateTimeDisplay = "19/06/2026 09:10",
                status = "Confirmado",
                description = "Avaliação (5★) do atendimento RMA-2026-0490 - Fonte Chaveada 12V 10A"
            ),
            CreditTransaction(
                id = "tx_base",
                customerId = "usr_segurtech",
                rmaId = null,
                rmaNumber = null,
                type = "Crédito por avaliação",
                amount = 2.36,
                dateTimeDisplay = "01/06/2026 08:00",
                status = "Confirmado",
                description = "Saldo acumulado em avaliações anteriores (236 avaliações computadas)"
            )
        )

        val INITIAL_NOTIFICATIONS = listOf(
            NotificationItem(
                id = "notif_01",
                customerId = "usr_segurtech",
                rmaId = "rma_01",
                rmaNumber = "RMA-2026-0842",
                title = "Ação necessária no seu RMA",
                message = "Existe uma etapa de pagamento pendente. Entre em contato com o Pós-Vendas para receber as orientações.",
                timestampDisplay = "26/10/2026 14:15",
                isRead = false,
                statusTrigger = RmaStatus.AGUARDANDO_PAGAMENTO
            ),
            NotificationItem(
                id = "notif_02",
                customerId = "usr_segurtech",
                rmaId = "rma_02",
                rmaNumber = "RMA-2026-0795",
                title = "Seu produto está disponível para retirada",
                message = "Seu produto chegou à loja e está disponível para retirada. Consulte o endereço na tela do RMA.",
                timestampDisplay = "25/10/2026 11:20",
                isRead = false,
                statusTrigger = RmaStatus.DISPONIVEL_RETIRADA
            ),
            NotificationItem(
                id = "notif_03",
                customerId = "usr_segurtech",
                rmaId = "rma_04",
                rmaNumber = "RMA-2026-0855",
                title = "Seu produto está retornando à loja",
                message = "O produto foi liberado para retorno à loja de origem. Aguarde a confirmação de disponibilidade antes de ir retirar.",
                timestampDisplay = "26/10/2026 17:00",
                isRead = true,
                statusTrigger = RmaStatus.LIBERADO
            ),
            NotificationItem(
                id = "notif_04",
                customerId = "usr_segurtech",
                rmaId = "rma_03",
                rmaNumber = "RMA-2026-0810",
                title = "Troca concluída",
                message = "A troca do seu produto foi registrada como concluída. Consulte os detalhes do atendimento no aplicativo.",
                timestampDisplay = "08/10/2026 15:30",
                isRead = true,
                statusTrigger = RmaStatus.TROCA
            )
        )
    }
}
