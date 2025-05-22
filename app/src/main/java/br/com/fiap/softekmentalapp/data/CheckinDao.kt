package br.com.fiap.softekmentalapp.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.softekmentalapp.model.Checkin

@Dao
interface CheckinDao {
    @Insert
    suspend fun insert(checkin: Checkin)

    @Query("SELECT * FROM checkins")
    suspend fun getAllCheckins(): List<Checkin>

    @Query("DELETE FROM checkins")
    suspend fun clearAllCheckins()
}