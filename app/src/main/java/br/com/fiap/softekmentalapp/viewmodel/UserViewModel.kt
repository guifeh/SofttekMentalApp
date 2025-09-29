package br.com.fiap.softekmentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.UserResponse
import br.com.fiap.softekmentalapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserState {
    object Idle : UserState()
    object Loading : UserState()
    data class Success(val data: Any) : UserState() // pode ser UserResponse ou lista
    data class Error(val message: String) : UserState()
}

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<UserState>(UserState.Idle)
    val state: StateFlow<UserState> = _state

    fun getUsers(token: String) {
        _state.value = UserState.Loading
        viewModelScope.launch {
            try {
                val users: List<UserResponse> = repository.getUsers(token)
                _state.value = UserState.Success(users)
            } catch (e: Exception) {
                _state.value = UserState.Error(e.message ?: "Erro ao buscar usuários")
            }
        }
    }

    fun getUser(token: String, id: String) {
        _state.value = UserState.Loading
        viewModelScope.launch {
            try {
                val user: UserResponse = repository.getUser(token, id)
                _state.value = UserState.Success(user)
            } catch (e: Exception) {
                _state.value = UserState.Error(e.message ?: "Erro ao buscar usuário")
            }
        }
    }

    fun resetState() {
        _state.value = UserState.Idle
    }
}
