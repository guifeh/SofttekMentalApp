package br.com.fiap.softekmentalapp.repository

import android.content.Context
import br.com.fiap.softekmentalapp.model.Checkin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CheckinRepository {
    private lateinit var checkinDao: CheckinDao

    fun initialize(context: Context) {
        checkinDao = AppDatabase.getDatabase(context).checkinDao()
    }

    suspend fun addCheckin(checkin: Checkin) {
        withContext(Dispatchers.IO) {
            checkinDao.insert(checkin)
        }
    }

    suspend fun getAllCheckins(): List<Checkin> {
        return withContext(Dispatchers.IO) {
            checkinDao.getAllCheckins()
        }
    }

    suspend fun clearAllCheckins() {
        withContext(Dispatchers.IO) {
            checkinDao.clearAllCheckins()
        }
    }
}