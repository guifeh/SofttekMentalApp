package br.com.fiap.softekmentalapp.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromMap(value: Map<Int, Int>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMap(value: String): Map<Int, Int> {
        val mapType = object : TypeToken<Map<Int, Int>>() {}.type
        return Gson().fromJson(value, mapType)
    }
}