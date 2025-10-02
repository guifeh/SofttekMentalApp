package br.com.fiap.softekmentalapp.model

data class AssessmentRequest(
    val question: String,
    val answer: Int
)

data class AssessmentResponse(
    val id: String,
    val question: String,
    val answer: Int,
    val score: String,
    val createdAt: Long
)

data class AssessmentSummaryResponse(
    val total: Int,
    val averageScore: Double,
    val status: String
)