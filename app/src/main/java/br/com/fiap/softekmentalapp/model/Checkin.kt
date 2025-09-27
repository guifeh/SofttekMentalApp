package br.com.fiap.softekmentalapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Checkin(
    val id: String? = null,
    val emotion: String,
    @SerialName("createdAt") val timestamp: Long = System.currentTimeMillis()
)
