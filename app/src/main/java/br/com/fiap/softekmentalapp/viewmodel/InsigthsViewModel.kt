package br.com.fiap.softekmentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.CheckinSummaryResponse
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InsightsViewModel(private val repository: CheckinRepository) : ViewModel() {

    private val _stats = MutableStateFlow<Map<String, Int>>(emptyMap())
    val stats: StateFlow<Map<String, Int>> = _stats

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadStats() {
        viewModelScope.launch {
            try {
                val summary: List<CheckinSummaryResponse> = repository.getCheckinSummary().summary
                _stats.value = summary.associate { it.emotion to it.count.toInt() }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            }
        }
    }
}
