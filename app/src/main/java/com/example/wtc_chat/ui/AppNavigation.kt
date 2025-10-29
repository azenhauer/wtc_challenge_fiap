package com.example.wtc_chat.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wtc_chat.data.model.UserRole
import com.example.wtc_chat.ui.screens.ChatScreen
import com.example.wtc_chat.ui.screens.CrmScreen
import com.example.wtc_chat.ui.screens.LandingScreen
import com.example.wtc_chat.ui.screens.LoginScreen
import com.example.wtc_chat.ui.screens.NewCampaignScreen
import com.example.wtc_chat.ui.screens.SignUpScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { userRole ->

                    navController.navigate("landing/${userRole.name}") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {

                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "landing/{userRole}",
            arguments = listOf(navArgument("userRole") { type = NavType.StringType })
        ) { backStackEntry ->
            val userRoleName = backStackEntry.arguments?.getString("userRole")
            val userRole = userRoleName?.let { UserRole.valueOf(it) } ?: UserRole.CLIENT // Default fallback

            LandingScreen(
                userRole = userRole,
                onNavigate = { role ->
                    val route = when (role) {
                        UserRole.CLIENT -> "chat"
                        UserRole.OPERATOR -> "crm"
                    }
                    navController.navigate(route) {

                        popUpTo("landing/{userRole}") { inclusive = true }
                    }
                }
            )
        }

        composable("chat") {
            ChatScreen()
        }
        composable("crm") {
            CrmScreen(onNavigateToNewCampaign = {
                navController.navigate("new_campaign")
            })
        }
        composable("new_campaign") {
            NewCampaignScreen()
        }
    }
}
