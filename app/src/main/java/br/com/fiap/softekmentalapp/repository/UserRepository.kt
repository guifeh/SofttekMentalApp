package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.RegisterRequest
import br.com.fiap.softekmentalapp.model.RegisterResponse
import br.com.fiap.softekmentalapp.model.UpdateUserRequest
import br.com.fiap.softekmentalapp.model.UserResponse
import br.com.fiap.softekmentalapp.network.RetrofitInstance
import retrofit2.Response

class UserRepository {
    private val api = RetrofitInstance.userApi

    suspend fun getUsers(token: String): List<UserResponse>{
        return api.getUsers("Bearer $token")
    }

    suspend fun getProfile(token: String): Response<UserResponse> {
        return api.getProfile("Bearer $token")
    }

    suspend fun updateProfile(userId: String, token: String, request: UpdateUserRequest): Response<UserResponse> {
        return api.updateProfile(userId, "Bearer $token", request)
    }

    suspend fun registerUser(request: RegisterRequest): RegisterResponse{
        return api.createUsers(request)
    }
}