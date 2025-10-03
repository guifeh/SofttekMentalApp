import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.RegisterRequest
import br.com.fiap.softekmentalapp.model.RegisterResponse
import br.com.fiap.softekmentalapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: RegisterResponse) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state

    fun register(name: String, email: String, password: String) {
        _state.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                val req = RegisterRequest(name, email, password)
                val response = repository.registerUser(req)
                _state.value = RegisterState.Success(response)
            } catch (e: Exception) {
                _state.value = RegisterState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    fun resetState() {
        _state.value = RegisterState.Idle
    }
}
