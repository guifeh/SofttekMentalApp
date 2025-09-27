package br.com.fiap.softekmentalapp.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: String,
    val createdAt: Long
)