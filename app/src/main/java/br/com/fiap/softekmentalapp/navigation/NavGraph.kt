package br.com.fiap.softekmentalapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.ui.components.MainScaffold
import br.com.fiap.softekmentalapp.ui.screens.*
import br.com.fiap.softekmentalapp.viewmodel.InsightsViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    coroutineScope: CoroutineScope
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Checkin.route
    ) {
        composable(AppScreen.Checkin.route) {
            MainScaffold(
                navController = navController,
                currentScreen = { CheckinScreen(navController, coroutineScope) },
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
                currentScreen = { HistoryScreen(navController) },
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
                currentScreen = { FeedbackScreen(emotion, navController) },
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
            MainScaffold(
                navController = navController,
                currentScreen = { InsightsScreen(viewModel = InsightsViewModel(CheckinRepository),
                        navController = navController,
                        isDarkTheme = isDarkTheme,
                        onThemeUpdated = onThemeUpdated
                    )
                },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }
    }
}