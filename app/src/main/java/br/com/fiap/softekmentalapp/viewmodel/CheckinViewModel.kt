package br.com.fiap.softekmentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.CheckinRequest
import br.com.fiap.softekmentalapp.model.CheckinResponse
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CheckinState {
    object Idle : CheckinState()
    object Loading : CheckinState()
    data class Success(val response: CheckinResponse) : CheckinState()
    data class Error(val message: String) : CheckinState()
}

class CheckinViewModel(
    private val repository: CheckinRepository = CheckinRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<CheckinState>(CheckinState.Idle)
    val state: StateFlow<CheckinState> = _state

    fun createCheckin(token: String, emotion: String, note: String?) {
        viewModelScope.launch {
            _state.value = CheckinState.Loading
            try {
                val request = CheckinRequest(
                    emotion = emotion,
                    note = note
                )
                val response = repository.createCheckin(token, request)
                _state.value = CheckinState.Success(response)
            } catch (e: Exception) {
                _state.value =
                    CheckinState.Error(e.message ?: "Erro inesperado ao registrar check-in")
            }
        }
    }

    fun resetState() {
        _state.value = CheckinState.Idle
    }
}
