package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.AssessmentRequest
import br.com.fiap.softekmentalapp.model.AssessmentResponse
import br.com.fiap.softekmentalapp.model.AssessmentSummaryResponse
import br.com.fiap.softekmentalapp.model.Question
import br.com.fiap.softekmentalapp.network.RetrofitInstance

class AssessmentRepository {
    private val api = RetrofitInstance.assessmentApi

    suspend fun createAssessment(token: String, request: AssessmentRequest): AssessmentResponse{
        return api.createAssessment("Bearer $token", request)
    }

    suspend fun getAssessment(token: String): List<AssessmentResponse>{
        return api.getAssessments("Bearer $token")
    }

    suspend fun getSummary(token: String): AssessmentSummaryResponse{
        return api.getSummary("Bearer $token")
    }

    fun getQuestions(): List<Question> {
        return listOf(
            Question(1, "Com que frequência você se sente sobrecarregado(a) com suas tarefas?"),
            Question(2, "Você sente que tem apoio suficiente no trabalho?"),
            Question(3, "Você tem dificuldades para dormir por conta de preocupações com o trabalho?")
        )
    }
}