package br.com.fiap.softekmentalapp.network

import br.com.fiap.softekmentalapp.model.Checkin
import br.com.fiap.softekmentalapp.model.CheckinSummary
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface  CheckinApiService{

    @POST("api/v1/checkins")
    suspend fun createCheckin(
        @Header("Authorization")token: String,
        @Body checkin: Checkin
    ): Checkin

    @GET("api/v1/checkins")
    suspend fun getCheckins(
        @Header("Authorization")token: String
    ): List<Checkin>

    @GET("api/v1/checkins/summary")
    suspend fun getCheckinSummary(
        @Header("Authorization") token: String
    ): CheckinSummary

    companion object {
        fun create(): CheckinApiService {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CheckinApiService::class.java)
        }
    }
}