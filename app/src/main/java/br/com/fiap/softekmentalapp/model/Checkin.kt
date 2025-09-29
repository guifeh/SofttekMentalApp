package br.com.fiap.softekmentalapp.model

data class CheckinRequest(
    val emotion: String,
    val note: String? = null
)

data class CheckinResponse(
    val id: String,
    val userId: String,
    val emotion: String,
    val note: String?,
    val timestamp: Long
)

data class CheckinSummaryItem(
    val emotion: String,
    val count: Int,
    val percentage: Double
)

data class CheckinSummaryResponse(
    val total: Int,
    val summary: List<CheckinSummaryItem>
)
