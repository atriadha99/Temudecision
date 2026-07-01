package com.andika.temudecision.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andika.temudecision.presentation.screen.*

@Composable
fun SDSSNavGraph(
    navController: NavHostController,
    activeStudyId: String?,
    onStudyChange: (String) -> Unit,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { 
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen()
        }

        composable("studies") {
            StudyManagementScreen(
                onStudyClick = { studyId ->
                    onStudyChange(studyId)
                    navController.navigate("dashboard")
                }
            )
        }

        composable("criteria") {
            if (activeStudyId != null) {
                CriteriaManagementScreen(studyId = activeStudyId)
            }
        }

        composable("alternatives") {
            if (activeStudyId != null) {
                AlternativeManagementScreen(studyId = activeStudyId)
            }
        }

        composable("scores") {
            if (activeStudyId != null) {
                ScoringMatrixScreen(studyId = activeStudyId)
            }
        }

        composable("calculate") {
            if (activeStudyId != null) {
                CalculationScreen(studyId = activeStudyId)
            }
        }
    }
}
