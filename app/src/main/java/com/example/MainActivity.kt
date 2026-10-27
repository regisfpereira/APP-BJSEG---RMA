package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.AppDestination
import com.example.ui.BjsegUiState
import com.example.ui.BjsegViewModel
import com.example.ui.MainNavTab
import com.example.ui.components.BjsegBottomNav
import com.example.ui.components.BjsegTopBar
import com.example.ui.components.DemoModeBadge
import com.example.ui.screens.CreditsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RatingScreen
import com.example.ui.screens.RmaDetailScreen
import com.example.ui.screens.RmaListScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: BjsegViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BjsegApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BjsegApp(viewModel: BjsegViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    if (uiState.currentUser == null) {
        LoginScreen(
            uiState = uiState,
            viewModel = viewModel
        )
    } else {
        val isSubScreen = uiState.destination !is AppDestination.MainTabs

        val topBarTitle = when (val dest = uiState.destination) {
            is AppDestination.RmaDetail -> uiState.selectedRma?.rmaNumber ?: "Ordem de Serviço"
            is AppDestination.RmaRating -> "Avaliar Atendimento"
            AppDestination.MainTabs -> when (uiState.currentTab) {
                MainNavTab.INICIO -> "BJSEG Pós-Vendas"
                MainNavTab.MEUS_RMAS -> "Meus RMAs"
                MainNavTab.NOTIFICACOES -> "Notificações"
                MainNavTab.CREDITOS -> "Meus Créditos"
                MainNavTab.PERFIL -> "Meu Perfil"
            }
        }

        BackHandler(enabled = isSubScreen || uiState.currentTab != MainNavTab.INICIO) {
            viewModel.navigateBack()
        }

        val context = androidx.compose.ui.platform.LocalContext.current
        val rightActionLabel = when (uiState.destination) {
            is AppDestination.RmaDetail -> if (uiState.selectedRma?.isPendingPayment == true) "Pagar" else "WhatsApp"
            else -> null
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Column {
                    BjsegTopBar(
                        title = topBarTitle,
                        canNavigateBack = isSubScreen,
                        onNavigateBack = { viewModel.navigateBack() },
                        unreadCount = uiState.unreadNotifCount,
                        onNotificationsClick = { viewModel.onTabSelected(MainNavTab.NOTIFICACOES) },
                        onProfileClick = { viewModel.onTabSelected(MainNavTab.PERFIL) },
                        rightActionButtonLabel = rightActionLabel,
                        onRightActionButtonClick = {
                            val rma = uiState.selectedRma
                            if (rma != null) {
                                val url = viewModel.getWhatsAppUrl(rma.rmaNumber, isPayment = rma.isPendingPayment)
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                context.startActivity(intent)
                            }
                        }
                    )
                    if (uiState.isDemoMode) {
                        DemoModeBadge()
                    }
                }
            },
            bottomBar = {
                if (!isSubScreen) {
                    BjsegBottomNav(
                        selectedTab = uiState.currentTab,
                        onTabSelected = { viewModel.onTabSelected(it) },
                        unreadNotifs = uiState.unreadNotifCount
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = Pair(uiState.destination, uiState.currentTab),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "AppScreenTransition"
                ) { (destination, tab) ->
                    when (destination) {
                        is AppDestination.RmaDetail -> {
                            val rma = uiState.selectedRma
                            if (rma != null) {
                                RmaDetailScreen(
                                    rma = rma,
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            } else {
                                RmaListScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            }
                        }
                        is AppDestination.RmaRating -> {
                            val rma = uiState.selectedRma
                            if (rma != null) {
                                RatingScreen(
                                    rma = rma,
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            } else {
                                RmaListScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            }
                        }
                        AppDestination.MainTabs -> {
                            when (tab) {
                                MainNavTab.INICIO -> HomeScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                                MainNavTab.MEUS_RMAS -> RmaListScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                                MainNavTab.NOTIFICACOES -> NotificationsScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                                MainNavTab.CREDITOS -> CreditsScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                                MainNavTab.PERFIL -> ProfileScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
