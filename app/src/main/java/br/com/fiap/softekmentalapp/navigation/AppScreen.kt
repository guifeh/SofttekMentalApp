package br.com.fiap.softekmentalapp.navigation

enum class AppScreen(val route: String, val requiresToken: Boolean) {
    Profile("profile", true),
    Checkin("checkin", true),
    RiskAssessment("riskAssessment", true),
    History("history", true),
    Insights("insights", true),
    Support("support", true),
    Settings("settings", false)
}

