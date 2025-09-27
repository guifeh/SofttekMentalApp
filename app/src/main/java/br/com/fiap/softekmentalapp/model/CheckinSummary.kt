package br.com.fiap.softekmentalapp.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckinSummaryResponse(
    val emotion: String,
    val count: Int
)

@Serializable
data class CheckinSummary(
    val totalCheckins: Int,
    val summary: List<CheckinSummaryResponse>
)