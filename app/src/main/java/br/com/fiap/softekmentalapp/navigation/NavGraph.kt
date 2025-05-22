package br.com.fiap.softekmentalapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.fiap.softekmentalapp.ui.components.MainScaffold
import br.com.fiap.softekmentalapp.ui.screens.CheckinScreen
import br.com.fiap.softekmentalapp.ui.screens.FeedbackScreen
import br.com.fiap.softekmentalapp.ui.screens.HistoryScreen
import br.com.fiap.softekmentalapp.ui.screens.RiskAssessmentScreen
import br.com.fiap.softekmentalapp.ui.screens.SupportScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Checkin.route
    ) {
        composable(AppScreen.Checkin.route) {
            MainScaffold(navController) {
                CheckinScreen(navController)
            }
        }
        composable(AppScreen.RiskAssessment.route) {
            MainScaffold(navController) {
                RiskAssessmentScreen(navController)
            }
        }
        composable(AppScreen.History.route) {
            MainScaffold(navController) {
                HistoryScreen(navController)
            }
        }
        composable(
            route = AppScreen.Feedback.route,
            arguments = listOf(navArgument("emotion") { type = NavType.StringType })
        ) { backStackEntry ->
            val emotion = backStackEntry.arguments?.getString("emotion") ?: ""
            MainScaffold(navController) {
                FeedbackScreen(emotion = emotion, navController = navController)
            }
        }

        // Nova rota da SupportScreen
        composable(AppScreen.Support.route) {
            MainScaffold(navController) {
                SupportScreen(navController)
            }
        }
    }
}

