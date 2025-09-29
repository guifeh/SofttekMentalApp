package br.com.fiap.softekmentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.LoginResponse
import br.com.fiap.softekmentalapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState{
    object Idle : AuthState()
    object Loading: AuthState()
    data class Success(val response: LoginResponse): AuthState()
    data class Error(val message: String): AuthState()
}

class AuthViewModel (
    private val repository: AuthRepository = AuthRepository()
): ViewModel(){

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String){
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val response = repository.login(email, password)
                _state.value = AuthState.Success(response)
            }catch (e: Exception){
                _state.value = AuthState.Error(e.message ?: "Ocorreu um erro inesperado no login")
            }
        }
    }
    fun resetState() {
        _state.value = AuthState.Idle
    }
}