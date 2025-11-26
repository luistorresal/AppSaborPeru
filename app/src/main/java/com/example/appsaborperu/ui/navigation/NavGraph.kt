package com.example.appsaborperu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appsaborperu.ui.auth.LoginScreen
import com.example.appsaborperu.ui.products.ProductListScreen
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // Pantalla de Login
        composable(Routes.LOGIN) {
            LoginScreen(
                onSuccess = { userName, userEmail, userRole ->
                    // Codificar el email para la URL
                    val encodedEmail = URLEncoder.encode(userEmail, "UTF-8")
                    navController.navigate("${Routes.PRODUCT_LIST}/$userName/$encodedEmail/$userRole") {
                        // Limpiar el back stack para que no pueda volver al login con back
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Productos con información del usuario
        composable("${Routes.PRODUCT_LIST}/{userName}/{userEmail}/{userRole}") { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val userEmail = URLDecoder.decode(
                backStackEntry.arguments?.getString("userEmail") ?: "", 
                "UTF-8"
            )
            val userRole = backStackEntry.arguments?.getString("userRole") ?: ""
            
            ProductListScreen(
                userName = userName,
                userEmail = userEmail,
                userRole = userRole,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
