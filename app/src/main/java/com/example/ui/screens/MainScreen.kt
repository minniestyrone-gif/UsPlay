package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.UsPlayViewModel
import com.example.ui.theme.UsPlayPlumBackground
import com.example.ui.theme.UsPlayPlumCardElevated
import com.example.ui.theme.UsPlayRoseDark
import com.example.ui.theme.UsPlayRosePrimary
import com.example.ui.theme.UsPlayTextMuted

sealed class BottomTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomTab("home", "HOME", Icons.Filled.Home, Icons.Outlined.Home)
    object Dates : BottomTab("dates", "DATES", Icons.Filled.DateRange, Icons.Outlined.DateRange)
    object Us : BottomTab("us", "US", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Profile : BottomTab("profile", "PROFILE", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MainScreen(
    viewModel: UsPlayViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var datesSubTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        BottomTab.Home,
        BottomTab.Dates,
        BottomTab.Us,
        BottomTab.Profile
    )

    val profile by viewModel.coupleProfile.collectAsState()

    val userMessage by viewModel.userMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = UsPlayPlumBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = UsPlayPlumCardElevated,
                contentColor = Color.White,
                tonalElevation = 8.dp
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        modifier = Modifier.testTag("nav_tab_${tab.route}"),
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                tint = if (isSelected) UsPlayRosePrimary else UsPlayTextMuted
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) UsPlayRosePrimary else UsPlayTextMuted
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = UsPlayRoseDark.copy(alpha = 0.4f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(UsPlayPlumBackground)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDatesTab = { targetSubTab ->
                        datesSubTab = targetSubTab
                        selectedTab = 1
                    }
                )
                1 -> DatesScreen(
                    viewModel = viewModel,
                    initialTab = datesSubTab
                )
                2 -> UsScreen(viewModel = viewModel)
                3 -> ProfileScreen(viewModel = viewModel)
            }
        }
    }
}
