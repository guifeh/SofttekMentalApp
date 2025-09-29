package br.com.fiap.softekmentalapp.network

import br.com.fiap.softekmentalapp.model.*
import retrofit2.http.*

interface AuthApi {
    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

interface UserApi {
    @GET("/api/v1/users")
    suspend fun getUsers(@Header("Authorization") token: String): List<UserResponse>

    @GET("/api/v1/users/{id}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): UserResponse
}

interface CheckinApi {
    @POST("/api/v1/checkins")
    suspend fun createCheckin(
        @Header("Authorization") token: String,
        @Body request: CheckinRequest
    ): CheckinResponse

    @GET("/api/v1/checkins")
    suspend fun getCheckins(@Header("Authorization") token: String): List<CheckinResponse>

    @DELETE("/api/v1/checkins/{id}")
    suspend fun deleteCheckin(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )

    @GET("/api/v1/checkins/report")
    suspend fun getReport(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("userId") userId: String? = null
    ): List<CheckinResponse>

    @GET("/api/v1/checkins/report/summary")
    suspend fun getSummary(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("userId") userId: String? = null
    ): CheckinSummaryResponse
}

interface AssessmentApi {
    @POST("/api/v1/assessments")
    suspend fun createAssessment(
        @Header("Authorization") token: String,
        @Body request: AssessmentRequest
    ): AssessmentResponse

    @GET("/api/v1/assessments")
    suspend fun getAssessments(@Header("Authorization") token: String): List<AssessmentResponse>

    @GET("/api/v1/assessments/summary")
    suspend fun getSummary(@Header("Authorization") token: String): AssessmentSummaryResponse
}
