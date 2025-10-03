package br.com.fiap.softekmentalapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.UpdateUserRequest
import br.com.fiap.softekmentalapp.model.UserResponse
import br.com.fiap.softekmentalapp.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository = UserRepository() ) : ViewModel() {

    var user by mutableStateOf<UserResponse?>(null)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadProfile(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.getProfile(token)
                if (response.isSuccessful) {
                    user = response.body()
                } else {
                    errorMessage = "Erro ao carregar perfil"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun updateProfile(userId: String, token: String, request: UpdateUserRequest) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.updateProfile(userId, token, request)
                if (response.isSuccessful) {
                    user = response.body()
                } else {
                    errorMessage = "Erro ao atualizar perfil: ${response.code()} - ${response.errorBody()?.string()}"
                    Log.e("ProfileViewModel", "Erro update: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
