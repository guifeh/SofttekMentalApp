package br.com.fiap.softekmentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InsightsViewModel(private val repository: CheckinRepository) : ViewModel() {

    private val _stats = MutableStateFlow<Map<String, Int>>(emptyMap())
    val stats: StateFlow<Map<String, Int>> = _stats

    init {
        viewModelScope.launch {
            _stats.value = repository.getCheckinStats()
        }
    }
}
