package br.com.fiap.softekmentalapp.model

data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val role: String,
    val createdAt: Long
)

data class UpdateUserRequest(
    val name: String,
    val email: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val name: String,
    val email: String,
    val id: String,
    val role: String,
    val createdAt: Long
)