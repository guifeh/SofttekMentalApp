package br.com.fiap.softekmentalapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.repository.AuthRepository
import br.com.fiap.softekmentalapp.ui.components.MainScaffold
import br.com.fiap.softekmentalapp.ui.screens.*
import br.com.fiap.softekmentalapp.viewmodel.InsightsViewModel
import br.com.fiap.softekmentalapp.viewmodel.CheckinViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    coroutineScope: CoroutineScope,
    checkinRepository: CheckinRepository,
    authRepository: AuthRepository
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(AppScreen.Checkin.route) {
            val viewModel = remember { CheckinViewModel(checkinRepository) }

            MainScaffold(
                navController = navController,
                currentScreen = {
                    CheckinScreen(
                        navController = navController,
                        coroutineScope = coroutineScope,
                        viewModel = viewModel
                    )
                },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }

        composable(AppScreen.RiskAssessment.route) {
            MainScaffold(
                navController = navController,
                currentScreen = { RiskAssessmentScreen(navController) },
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

        composable("login") {
            LoginScreen(
                authRepository = authRepository,
                onLoginSuccess = {
                    navController.navigate("checkin") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}
