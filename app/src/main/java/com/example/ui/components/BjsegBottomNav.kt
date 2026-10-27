package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainNavTab
import com.example.ui.theme.BlackMain
import com.example.ui.theme.Gray200
import com.example.ui.theme.Gray400
import com.example.ui.theme.Gray500
import com.example.ui.theme.Orange500
import com.example.ui.theme.WhitePure

@Composable
fun BjsegBottomNav(
    selectedTab: MainNavTab,
    onTabSelected: (MainNavTab) -> Unit,
    unreadNotifs: Int = 0,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .background(WhitePure)
    ) {
        HorizontalDivider(color = Gray200, thickness = 1.dp)

        NavigationBar(
            containerColor = WhitePure,
            tonalElevation = 0.dp
        ) {
            // Tab 1: Início
            NavigationBarItem(
                selected = selectedTab == MainNavTab.INICIO,
                onClick = { onTabSelected(MainNavTab.INICIO) },
                icon = {
                    Icon(
                        imageVector = if (selectedTab == MainNavTab.INICIO) Icons.Default.Home else Icons.Outlined.Home,
                        contentDescription = "Início",
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Início",
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == MainNavTab.INICIO) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BlackMain,
                    selectedTextColor = BlackMain,
                    unselectedIconColor = Gray400,
                    unselectedTextColor = Gray500,
                    indicatorColor = Gray200.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("nav_tab_inicio")
            )

            // Tab 2: Meus RMAs
            NavigationBarItem(
                selected = selectedTab == MainNavTab.MEUS_RMAS,
                onClick = { onTabSelected(MainNavTab.MEUS_RMAS) },
                icon = {
                    Icon(
                        imageVector = if (selectedTab == MainNavTab.MEUS_RMAS) Icons.Default.Build else Icons.Outlined.Build,
                        contentDescription = "Meus RMAs",
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Meus RMAs",
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == MainNavTab.MEUS_RMAS) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BlackMain,
                    selectedTextColor = BlackMain,
                    unselectedIconColor = Gray400,
                    unselectedTextColor = Gray500,
                    indicatorColor = Gray200.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("nav_tab_rmas")
            )

            // Tab 3: Notificações
            NavigationBarItem(
                selected = selectedTab == MainNavTab.NOTIFICACOES,
                onClick = { onTabSelected(MainNavTab.NOTIFICACOES) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (unreadNotifs > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Orange500)
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedTab == MainNavTab.NOTIFICACOES) Icons.Default.Notifications else Icons.Outlined.Notifications,
                            contentDescription = "Notificações",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = "Notificações",
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == MainNavTab.NOTIFICACOES) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BlackMain,
                    selectedTextColor = BlackMain,
                    unselectedIconColor = Gray400,
                    unselectedTextColor = Gray500,
                    indicatorColor = Gray200.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("nav_tab_notificacoes")
            )

            // Tab 4: Meus Créditos
            NavigationBarItem(
                selected = selectedTab == MainNavTab.CREDITOS,
                onClick = { onTabSelected(MainNavTab.CREDITOS) },
                icon = {
                    Icon(
                        imageVector = if (selectedTab == MainNavTab.CREDITOS) Icons.Default.Star else Icons.Outlined.Star,
                        contentDescription = "Meus créditos",
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Créditos",
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == MainNavTab.CREDITOS) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BlackMain,
                    selectedTextColor = BlackMain,
                    unselectedIconColor = Gray400,
                    unselectedTextColor = Gray500,
                    indicatorColor = Gray200.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("nav_tab_creditos")
            )

            // Tab 5: Perfil
            NavigationBarItem(
                selected = selectedTab == MainNavTab.PERFIL,
                onClick = { onTabSelected(MainNavTab.PERFIL) },
                icon = {
                    Icon(
                        imageVector = if (selectedTab == MainNavTab.PERFIL) Icons.Default.Person else Icons.Outlined.Person,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = "Perfil",
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == MainNavTab.PERFIL) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BlackMain,
                    selectedTextColor = BlackMain,
                    unselectedIconColor = Gray400,
                    unselectedTextColor = Gray500,
                    indicatorColor = Gray200.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("nav_tab_perfil")
            )
        }
    }
}
