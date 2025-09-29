package br.com.fiap.softekmentalapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
    val checkinApi: CheckinApi by lazy { retrofit.create(CheckinApi::class.java) }
    val assessmentApi: AssessmentApi by lazy { retrofit.create(AssessmentApi::class.java) }
}