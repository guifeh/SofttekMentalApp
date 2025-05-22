package br.com.fiap.softekmentalapp.repository

import androidx.compose.runtime.mutableStateListOf
import br.com.fiap.softekmentalapp.model.Checkin

object CheckinRepository {
    private val checkins = mutableStateListOf<Checkin>()

    fun addCheckin(checkin: Checkin) {
        checkins.add(checkin)
    }

    fun getAllCheckins(): List<Checkin> {
        return checkins
    }

    fun clearAllCheckins() {
        checkins.clear()
    }
}
