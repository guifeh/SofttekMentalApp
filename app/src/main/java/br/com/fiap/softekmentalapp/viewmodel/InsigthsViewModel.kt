package br.com.fiap.softekmentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.CheckinResponse
import br.com.fiap.softekmentalapp.model.CheckinSummaryResponse
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InsightsViewModel(private val repository: CheckinRepository) : ViewModel() {

    private val _checkins = MutableStateFlow<List<CheckinResponse>>(emptyList())
    val checkins: StateFlow<List<CheckinResponse>> = _checkins

    fun loadCheckins(token: String) {
        viewModelScope.launch {
            try {
                val checkins = repository.getCheckins(token)
                _checkins.value = checkins
            } catch (e: Exception) {
                e.printStackTrace()
                _checkins.value = emptyList()
            }
        }
    }
}

