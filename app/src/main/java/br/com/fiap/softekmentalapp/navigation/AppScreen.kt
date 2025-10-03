package br.com.fiap.softekmentalapp.navigation

sealed class AppScreen(val route: String) {
    object Checkin : AppScreen("checkin")
    object RiskAssessment : AppScreen("risk_assessment")

    object History : AppScreen("history")

    object Support : AppScreen("support")
    object Settings : AppScreen("settings")

    object Insights : AppScreen("insights")

    object Profile : AppScreen("profile")
}
