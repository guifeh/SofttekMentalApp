package br.com.fiap.softekmentalapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.fiap.softekmentalapp.repository.AuthRepository
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.ui.components.MainScaffold
import br.com.fiap.softekmentalapp.ui.screens.*
import br.com.fiap.softekmentalapp.viewmodel.AssessmentViewModel
import br.com.fiap.softekmentalapp.viewmodel.CheckinViewModel
import br.com.fiap.softekmentalapp.viewmodel.InsightsViewModel
import com.example.app.ui.screens.RegisterScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    checkinRepository: CheckinRepository,
    authRepository: AuthRepository,
) {
    NavHost(
        navController = navController,
        startDestination = "register"
    ) {

        composable("profile/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")?: ""
            MainScaffold(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated,
                token = token,
                currentScreen = {
                    ProfileScreen(
                        viewModel = viewModel(),
                        token = token,
                        onLogout = {
                            navController.navigate("login"){
                                popUpTo("profile/{token}"){inclusive = true}
                            }
                        }
                    )
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // LOGIN
        composable("login") {
            LoginScreen(
                authViewModel = viewModel(),
                onLoginSuccess = { token ->
                    navController.navigate("checkin/$token") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // CHECKIN
        composable("${AppScreen.Checkin.route}/{token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val checkinVm = remember { CheckinViewModel(checkinRepository) }

            MainScaffold(
                navController = navController,
                currentScreen = {
                    CheckinScreen(
                        token = token,
                        navController = navController,
                        checkinViewModel = checkinVm
                    )
                },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated,
                token = token
            )
        }

        // RISK ASSESSMENT
        composable(
            route = "${AppScreen.RiskAssessment.route}/{token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val assessmentVm: AssessmentViewModel = viewModel()

            MainScaffold(
                navController = navController,
                currentScreen = {
                    RiskAssessmentScreen(
                        navController = navController,
                        token = token,
                        assessmentViewModel = assessmentVm
                    )
                },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated,
                token = token
            )
        }

        // HISTORY
        composable(
            route = "${AppScreen.History.route}/{token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""

            MainScaffold(
                navController = navController,
                currentScreen = {
                    HistoryScreen(
                        navController = navController,
                        checkinRepository = checkinRepository,
                        token = token
                    )
                },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated,
                token = token
            )
        }

        composable(
            route = "${AppScreen.Support.route}/{token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ){ backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            MainScaffold(
                navController = navController,
                currentScreen = { SupportScreen(navController = navController, token = token) },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated
            )
        }

        // INSIGHTS
        composable(
            route = "${AppScreen.Insights.route}/{token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val insightsVm = remember { InsightsViewModel(checkinRepository) }

            MainScaffold(
                navController = navController,
                currentScreen = {
                    InsightsScreen(viewModel = insightsVm, token = token)
                },
                isDarkTheme = isDarkTheme,
                onThemeUpdated = onThemeUpdated,
                token = token
            )
        }
    }
}
