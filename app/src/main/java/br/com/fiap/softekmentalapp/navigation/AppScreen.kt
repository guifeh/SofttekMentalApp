package br.com.fiap.softekmentalapp.navigation

sealed class AppScreen(val route: String) {
    object Checkin : AppScreen("checkin")
    object RiskAssessment : AppScreen("risk_assessment")

    object Feedback : AppScreen("feedback/{emotion}") {
        fun createRoute(emotion: String) = "feedback/$emotion"
    }

    object History : AppScreen("history")

    object Support : AppScreen("support")
}
