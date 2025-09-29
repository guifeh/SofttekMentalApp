package br.com.fiap.softekmentalapp.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val user: UserResponse
)