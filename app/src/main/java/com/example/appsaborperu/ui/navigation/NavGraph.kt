package com.example.appsaborperu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appsaborperu.ui.auth.LoginScreen
import com.example.appsaborperu.ui.home.HomeScreen
import com.example.appsaborperu.ui.products.ProductListScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onSuccess = { userName ->
                    navController.navigate("${Routes.HOME}/$userName")
                }
            )
        }

        composable("${Routes.HOME}/{userName}") { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            HomeScreen(
                userName = userName,
                onExploreClick = {
                    navController.navigate(Routes.PRODUCT_LIST)
                }
            )
        }

        composable(Routes.PRODUCT_LIST) {
            ProductListScreen()
        }
    }
}
