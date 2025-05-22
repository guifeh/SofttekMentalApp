package br.com.fiap.softekmentalapp.model

data class Checkin(
    val emotion: String,
    val timestamp: Long = System.currentTimeMillis()
)