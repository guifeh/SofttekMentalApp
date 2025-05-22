package br.com.fiap.softekmentalapp.model

data class AssessmentResult(
    val timestamp: Long,
    val responses: Map<Int, Int>,
    val score: Double,
    val classification: String
)
