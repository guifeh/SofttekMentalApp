package br.com.fiap.softekmentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.AssessmentRequest
import br.com.fiap.softekmentalapp.model.AssessmentResponse
import br.com.fiap.softekmentalapp.model.Question
import br.com.fiap.softekmentalapp.repository.AssessmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AssessmentState {
    object Idle : AssessmentState()
    object Loading : AssessmentState()
    data class Success(val data: Any) : AssessmentState()
    data class Error(val message: String) : AssessmentState()
}

class AssessmentViewModel(
    private val repository: AssessmentRepository = AssessmentRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<AssessmentState>(AssessmentState.Idle)
    val state: StateFlow<AssessmentState> = _state

    fun createAssessment(token: String, request: AssessmentRequest) {
        viewModelScope.launch {
            _state.value = AssessmentState.Loading
            try {
                val response = repository.createAssessment(token, request)
                _state.value = AssessmentState.Success(response)
            } catch (e: Exception) {
                _state.value = AssessmentState.Error(e.message ?: "Erro ao criar avaliação")
            }
        }
    }

    fun submitBatch(token: String, requests: List<AssessmentRequest>) {
        viewModelScope.launch {
            _state.value = AssessmentState.Loading
            try {
                requests.forEach { req ->
                    repository.createAssessment(token,req)
                }
                val summary = repository.getSummary(token)
                _state.value = AssessmentState.Success(summary)
            } catch (e: Exception) {
                _state.value = AssessmentState.Error(e.message ?: "Erro ao enviar avaliações")
            }
        }
    }

    fun getQuestions(): List<Question> {
        return repository.getQuestions()
    }

    fun getAssessments(token: String) {
        _state.value = AssessmentState.Loading
        viewModelScope.launch {
            try {
                val list = repository.getAssessment(token)
                _state.value = AssessmentState.Success(list)
            } catch (e: Exception) {
                _state.value = AssessmentState.Error(e.message ?: "Erro ao carregar avaliações")
            }
        }
    }

    fun getSummary(token: String) {
        _state.value = AssessmentState.Loading
        viewModelScope.launch {
            try {
                val summary = repository.getSummary(token)
                _state.value = AssessmentState.Success(summary)
            } catch (e: Exception) {
                _state.value = AssessmentState.Error(e.message ?: "Erro ao carregar resumo")
            }
        }
    }

    fun resetState() {
        _state.value = AssessmentState.Idle
    }
}
