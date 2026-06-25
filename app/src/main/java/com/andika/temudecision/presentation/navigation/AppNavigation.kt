package com.andika.temudecision.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andika.temudecision.presentation.screen.CategoryDetailScreen
import com.andika.temudecision.presentation.screen.DashboardScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                onCategoryClick = { id, name -> 
                    navController.navigate("detail/$id/$name") 
                }
            )
        }
        composable(
            "detail/{id}/{name}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val name = backStackEntry.arguments?.getString("name") ?: ""
            CategoryDetailScreen(
                categoryId = id,
                categoryName = name,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
