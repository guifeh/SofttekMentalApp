package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.LoginRequest
import br.com.fiap.softekmentalapp.model.LoginResponse
import br.com.fiap.softekmentalapp.network.RetrofitInstance

class AuthRepository {
    private val api = RetrofitInstance.authApi

    suspend fun login(email: String, password: String): LoginResponse {
        return api.login(LoginRequest(email, password))
    }
}
