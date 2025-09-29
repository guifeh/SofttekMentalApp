package br.com.fiap.softekmentalapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.ui.components.MainScaffold
import br.com.fiap.softekmentalapp.ui.screens.*
import br.com.fiap.softekmentalapp.viewmodel.AssessmentViewModel
import br.com.fiap.softekmentalapp.viewmodel.InsightsViewModel
import br.com.fiap.softekmentalapp.viewmodel.CheckinViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    checkinRepository: CheckinRepository
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = viewModel(),
                onLoginSuccess = { token ->
                    navController.navigate("checkin/$token") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "checkin/{token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val viewModel = remember { CheckinViewModel(checkinRepository) }

            MainScaffold(
                navController = navController,
                currentScreen = {
                    CheckinScreen(
                        token = token,
                        checkinViewModel = viewModel
                    )
                },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }

        composable(AppScreen.RiskAssessment.route) {
            MainScaffold(
                navController = navController,
                currentScreen = { RiskAssessmentScreen(token = String(), assessmentViewModel = viewModel()) },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }

        composable(AppScreen.History.route) {
            MainScaffold(
                navController = navController,
                currentScreen = { HistoryScreen(navController, checkinRepository) },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }

        composable(
            route = AppScreen.Feedback.route,
            arguments = listOf(navArgument("emotion") { type = NavType.StringType })
        ) { backStackEntry ->
            val emotion = backStackEntry.arguments?.getString("emotion") ?: ""
            MainScaffold(
                navController = navController,
                currentScreen = {
                    FeedbackScreen(
                        emotion = emotion,
                        navController = navController,
                        repository = checkinRepository
                    )
                },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }

        composable(AppScreen.Support.route) {
            MainScaffold(
                navController = navController,
                currentScreen = { SupportScreen(navController) },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }

        composable(AppScreen.Insights.route) {
            val viewModel = remember { InsightsViewModel(checkinRepository) }

            MainScaffold(
                navController = navController,
                currentScreen = { InsightsScreen(viewModel = viewModel) },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }
    }
}
