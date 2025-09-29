package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.UserResponse
import br.com.fiap.softekmentalapp.network.RetrofitInstance

class UserRepository {
    private val api = RetrofitInstance.userApi

    suspend fun getUsers(token: String): List<UserResponse>{
        return api.getUsers("Bearer $token")
    }

    suspend fun getUser(token: String, id: String): UserResponse{
        return api.getUser("Bearer $token", id)
    }
}