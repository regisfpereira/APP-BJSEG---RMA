package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BjsegRepository
import com.example.model.CreditWallet
import com.example.model.NotificationItem
import com.example.model.RmaItem
import com.example.model.RmaStatus
import com.example.model.RmaType
import com.example.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainNavTab(val label: String) {
    INICIO("Início"),
    MEUS_RMAS("Meus RMAs"),
    NOTIFICACOES("Notificações"),
    CREDITOS("Meus créditos"),
    PERFIL("Perfil")
}

enum class RmaFilterCategory(val label: String) {
    TODOS("Todos"),
    EM_ANDAMENTO("Em andamento"),
    FINALIZADOS("Finalizados"),
    GARANTIA("Garantia"),
    CLIENTE("Manutenção cliente"),
    TROCA("Troca")
}

sealed interface AppDestination {
    data object MainTabs : AppDestination
    data class RmaDetail(val rmaId: String) : AppDestination
    data class RmaRating(val rmaId: String) : AppDestination
}

data class BjsegUiState(
    val currentUser: UserAccount? = null,
    val isDemoMode: Boolean = true,
    val currentTab: MainNavTab = MainNavTab.INICIO,
    val destination: AppDestination = AppDestination.MainTabs,
    val rmas: List<RmaItem> = emptyList(),
    val filteredRmas: List<RmaItem> = emptyList(),
    val selectedFilter: RmaFilterCategory = RmaFilterCategory.TODOS,
    val searchQuery: String = "",
    val selectedRma: RmaItem? = null,
    val creditWallet: CreditWallet? = null,
    val notifications: List<NotificationItem> = emptyList(),
    val unreadNotifCount: Int = 0,
    val urgentPaymentRma: RmaItem? = null,
    val urgentPickupRma: RmaItem? = null,
    val whatsAppNumber: String = "551146467000",

    // Login Flow
    val loginDoc: String = "",
    val loginOtp: String = "",
    val isOtpStep: Boolean = false,
    val loginError: String? = null,
    val loginLoading: Boolean = false,
    val pendingDemoOtp: String? = null,

    // Rating Flow
    val ratingStars: Int = 5,
    val ratingComment: String = "",
    val isSubmittingRating: Boolean = false,
    val ratingSuccessMessage: String? = null,
    val ratingErrorMessage: String? = null,

    // Feedback SnackBar
    val snackbarMessage: String? = null
)

class BjsegViewModel(
    private val repository: BjsegRepository = BjsegRepository.getInstance()
) : ViewModel() {

    private val _currentTab = MutableStateFlow(MainNavTab.INICIO)
    private val _destination = MutableStateFlow<AppDestination>(AppDestination.MainTabs)
    private val _selectedFilter = MutableStateFlow(RmaFilterCategory.TODOS)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedRmaId = MutableStateFlow<String?>(null)

    // Login screen states
    private val _loginDoc = MutableStateFlow("12.345.678/0001-90")
    private val _loginOtp = MutableStateFlow("")
    private val _isOtpStep = MutableStateFlow(false)
    private val _loginError = MutableStateFlow<String?>(null)
    private val _loginLoading = MutableStateFlow(false)

    // Rating flow
    private val _ratingStars = MutableStateFlow(5)
    private val _ratingComment = MutableStateFlow("")
    private val _isSubmittingRating = MutableStateFlow(false)
    private val _ratingSuccessMessage = MutableStateFlow<String?>(null)
    private val _ratingErrorMessage = MutableStateFlow<String?>(null)

    private val _snackbarMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BjsegUiState> = combine(
        repository.currentUser,
        repository.rmas,
        repository.getCreditWallet(),
        repository.notifications,
        repository.whatsAppSupportNumber,
        repository.pendingOtp,
        _currentTab,
        _destination,
        _selectedFilter,
        _searchQuery,
        _selectedRmaId
    ) { args ->
        val user = args[0] as UserAccount?
        val allRmas = (args[1] as List<RmaItem>).filter { rma ->
            user == null || rma.customerId == user.id
        }
        val wallet = args[2] as CreditWallet
        val notifs = (args[3] as List<NotificationItem>).filter { n ->
            user == null || n.customerId == user.id
        }
        val waNum = args[4] as String
        val pendingOtp = args[5] as String?
        val tab = args[6] as MainNavTab
        val dest = args[7] as AppDestination
        val filter = args[8] as RmaFilterCategory
        val query = (args[9] as String).trim()
        val selRmaId = args[10] as String?

        // Compute filtered RMAs
        val queryLower = query.lowercase()
        val filtered = allRmas.filter { item ->
            val matchesCategory = when (filter) {
                RmaFilterCategory.TODOS -> true
                RmaFilterCategory.EM_ANDAMENTO -> !item.status.isFinished
                RmaFilterCategory.FINALIZADOS -> item.status.isFinished
                RmaFilterCategory.GARANTIA -> item.rmaType == RmaType.MANUTENCAO_GARANTIA
                RmaFilterCategory.CLIENTE -> item.rmaType == RmaType.MANUTENCAO_CLIENTE
                RmaFilterCategory.TROCA -> item.rmaType == RmaType.TROCA
            }
            val matchesSearch = query.isBlank() ||
                item.rmaNumber.lowercase().contains(queryLower) ||
                item.productName.lowercase().contains(queryLower) ||
                item.productCode.lowercase().contains(queryLower)

            matchesCategory && matchesSearch
        }

        val selRma = allRmas.firstOrNull { it.id == selRmaId }
        val unreadCount = notifs.count { !it.isRead }
        val urgentPayment = allRmas.firstOrNull { it.status == RmaStatus.AGUARDANDO_PAGAMENTO }
        val urgentPickup = allRmas.firstOrNull { it.status == RmaStatus.DISPONIVEL_RETIRADA }

        BjsegUiState(
            currentUser = user,
            isDemoMode = true,
            currentTab = tab,
            destination = dest,
            rmas = allRmas,
            filteredRmas = filtered,
            selectedFilter = filter,
            searchQuery = query,
            selectedRma = selRma,
            creditWallet = wallet,
            notifications = notifs,
            unreadNotifCount = unreadCount,
            urgentPaymentRma = urgentPayment,
            urgentPickupRma = urgentPickup,
            whatsAppNumber = waNum,
            loginDoc = _loginDoc.value,
            loginOtp = _loginOtp.value,
            isOtpStep = _isOtpStep.value,
            loginError = _loginError.value,
            loginLoading = _loginLoading.value,
            pendingDemoOtp = pendingOtp,
            ratingStars = _ratingStars.value,
            ratingComment = _ratingComment.value,
            isSubmittingRating = _isSubmittingRating.value,
            ratingSuccessMessage = _ratingSuccessMessage.value,
            ratingErrorMessage = _ratingErrorMessage.value,
            snackbarMessage = _snackbarMessage.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BjsegUiState()
    )

    fun onTabSelected(tab: MainNavTab) {
        _currentTab.value = tab
        _destination.value = AppDestination.MainTabs
    }

    fun onFilterSelected(filter: RmaFilterCategory) {
        _selectedFilter.value = filter
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun openRmaDetail(rmaId: String) {
        _selectedRmaId.value = rmaId
        _destination.value = AppDestination.RmaDetail(rmaId)
    }

    fun openRatingScreen(rmaId: String) {
        _selectedRmaId.value = rmaId
        _ratingStars.value = 5
        _ratingComment.value = ""
        _ratingSuccessMessage.value = null
        _ratingErrorMessage.value = null
        _destination.value = AppDestination.RmaRating(rmaId)
    }

    fun navigateBack() {
        when (_destination.value) {
            is AppDestination.RmaRating -> {
                val rmaId = _selectedRmaId.value
                if (rmaId != null) {
                    _destination.value = AppDestination.RmaDetail(rmaId)
                } else {
                    _destination.value = AppDestination.MainTabs
                }
            }
            is AppDestination.RmaDetail -> {
                _destination.value = AppDestination.MainTabs
            }
            AppDestination.MainTabs -> {
                if (_currentTab.value != MainNavTab.INICIO) {
                    _currentTab.value = MainNavTab.INICIO
                }
            }
        }
    }

    // Login methods
    fun onLoginDocChanged(input: String) {
        _loginDoc.value = UserAccount.formatDocument(input)
        _loginError.value = null
    }

    fun onLoginOtpChanged(input: String) {
        _loginOtp.value = input.filter { it.isDigit() }.take(6)
        _loginError.value = null
    }

    fun requestLoginOtp() {
        val doc = _loginDoc.value
        _loginLoading.value = true
        _loginError.value = null

        viewModelScope.launch {
            val result = repository.requestLogin(doc)
            _loginLoading.value = false
            result.onSuccess {
                _isOtpStep.value = true
                _loginOtp.value = ""
            }.onFailure { err ->
                _loginError.value = err.message ?: "Erro ao validar documento"
            }
        }
    }

    fun verifyLoginOtp() {
        val doc = _loginDoc.value
        val otp = _loginOtp.value
        if (otp.length < 6) {
            _loginError.value = "Digite os 6 dígitos do código de segurança recebido."
            return
        }

        _loginLoading.value = true
        _loginError.value = null

        viewModelScope.launch {
            val result = repository.verifyLoginOtp(doc, otp)
            _loginLoading.value = false
            result.onSuccess {
                _isOtpStep.value = false
                _loginOtp.value = ""
                _currentTab.value = MainNavTab.INICIO
                _destination.value = AppDestination.MainTabs
                showSnackbar("Bem-vindo(a) ao BJSEG Pós-Vendas!")
            }.onFailure { err ->
                _loginError.value = err.message ?: "Código inválido"
            }
        }
    }

    fun quickFillDemo(isCompany: Boolean) {
        if (isCompany) {
            _loginDoc.value = "12.345.678/0001-90"
        } else {
            _loginDoc.value = "123.456.789-00"
        }
        _isOtpStep.value = false
        _loginError.value = null
    }

    fun logout() {
        repository.logout()
        _isOtpStep.value = false
        _loginDoc.value = ""
        _loginOtp.value = ""
        _currentTab.value = MainNavTab.INICIO
        _destination.value = AppDestination.MainTabs
    }

    fun switchDemoUser(isCompany: Boolean) {
        repository.switchDemoUser(isCompany)
        showSnackbar("Perfil alternado para demonstração.")
    }

    // Rating methods
    fun setRatingStars(stars: Int) {
        _ratingStars.value = stars
    }

    fun setRatingComment(comment: String) {
        _ratingComment.value = comment
    }

    fun submitRating(rmaId: String) {
        _isSubmittingRating.value = true
        _ratingErrorMessage.value = null
        _ratingSuccessMessage.value = null

        viewModelScope.launch {
            val res = repository.submitRmaRating(
                rmaId = rmaId,
                stars = _ratingStars.value,
                comment = _ratingComment.value
            )
            _isSubmittingRating.value = false
            res.onSuccess {
                _ratingSuccessMessage.value = "Obrigado por avaliar seu atendimento! Seu crédito de R$ 0,01 foi registrado com sucesso."
                showSnackbar("Crédito de R$ 0,01 adicionado à sua carteira!")
            }.onFailure { error ->
                _ratingErrorMessage.value = error.message ?: "Falha ao registrar avaliação."
            }
        }
    }

    fun markNotificationAsRead(id: String) {
        repository.markNotificationAsRead(id)
    }

    fun markAllNotificationsAsRead() {
        repository.markAllNotificationsAsRead()
        showSnackbar("Todas as notificações foram marcadas como lidas.")
    }

    fun requestContactUpdate(email: String, phone: String, reason: String) {
        val result = repository.requestContactUpdate(email, phone, reason)
        result.onSuccess { msg ->
            showSnackbar(msg)
        }.onFailure { err ->
            showSnackbar(err.message ?: "Erro ao solicitar atualização.")
        }
    }

    fun getWhatsAppUrl(rmaNumber: String, isPayment: Boolean): String {
        return repository.buildWhatsAppUrl(rmaNumber, isPayment)
    }

    fun getGeneralWhatsAppUrl(): String {
        return repository.buildGeneralWhatsAppUrl()
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
