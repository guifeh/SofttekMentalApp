package br.com.fiap.softekmentalapp.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softekmentalapp.model.Checkin
import br.com.fiap.softekmentalapp.model.Emotion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import br.com.fiap.softekmentalapp.repository.CheckinRepository

class CheckinViewModel (
    private val repository: CheckinRepository
): ViewModel(){

    private val _checkins = MutableStateFlow<List<Checkin>>(emptyList())
    val checkins: StateFlow<List<Checkin>> = _checkins

    fun loadCheckins(){
        viewModelScope.launch {
            _checkins.value = repository.getAllCheckins()
        }
    }

    fun addCheckin(emotion: String){
        viewModelScope.launch {
            repository.addCheckin(Checkin(emotion = emotion))
            loadCheckins()
        }
    }
}