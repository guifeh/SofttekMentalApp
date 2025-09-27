package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val user: User
)

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

class AuthRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // 10.0.2.2 = localhost no emulador Android
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(AuthApi::class.java)

    suspend fun login(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            api.login(LoginRequest(email, password))
        }
    }
}
