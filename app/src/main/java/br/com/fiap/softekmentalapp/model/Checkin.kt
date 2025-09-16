package br.com.fiap.softekmentalapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checkins")
data class Checkin(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val emotion: String,
    val timestamp: Long = System.currentTimeMillis()
)