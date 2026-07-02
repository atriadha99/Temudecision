package com.andika.temudecision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.andika.temudecision.navigation.SDSSNavGraph
import com.andika.temudecision.presentation.components.SidebarDrawer
import com.andika.temudecision.presentation.components.TopBar
import com.andika.temudecision.presentation.components.BottomNavBar
import com.andika.temudecision.presentation.ui.theme.TemuDecisionTheme
import com.andika.temudecision.data.local.DatabaseInitializer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var dbInitializer: DatabaseInitializer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var activeStudyId by remember { mutableStateOf<String?>(null) }
            var activeStudyName by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                dbInitializer.seedIfNeeded()
            }

            TemuDecisionTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route ?: "dashboard"

                val showLayout = currentRoute != "login"

                if (showLayout) {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            SidebarDrawer(
                                activeRoute = currentRoute,
                                hasActiveStudy = activeStudyId != null,
                                onNavigate = { route ->
                                    navController.navigate(route)
                                    scope.launch { drawerState.close() }
                                },
                                onLogout = {
                                    activeStudyId = null
                                    activeStudyName = null
                                    navController.navigate("login") {
                                        popUpTo(0)
                                    }
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    ) {
                        Scaffold(
                            topBar = {
                                TopBar(
                                    activeStudyName = activeStudyName,
                                    isDarkMode = isDarkMode,
                                    onMenuClick = { scope.launch { drawerState.open() } },
                                    onToggleDarkMode = { isDarkMode = !isDarkMode },
                                    onStudySelectorClick = { navController.navigate("studies") }
                                )
                            },
                            bottomBar = {
                                BottomNavBar(
                                    activeRoute = currentRoute,
                                    hasActiveStudy = activeStudyId != null,
                                    onNavigate = { route -> navController.navigate(route) }
                                )
                            }
                        ) { padding ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                SDSSNavGraph(
                                    navController = navController,
                                    activeStudyId = activeStudyId,
                                    onStudyChange = { id ->
                                        activeStudyId = id
                                        scope.launch {
                                            val study = dbInitializer.getStudyName(id)
                                            activeStudyName = study
                                        }
                                    },
                                    onLogout = { navController.navigate("login") }
                                )
                            }
                        }
                    }
                } else {
                    SDSSNavGraph(
                        navController = navController,
                        activeStudyId = activeStudyId,
                        onStudyChange = { activeStudyId = it },
                        onLogout = { navController.navigate("login") }
                    )
                }
            }
        }
    }
}
