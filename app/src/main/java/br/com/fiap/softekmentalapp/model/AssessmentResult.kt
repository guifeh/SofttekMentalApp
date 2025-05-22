package br.com.fiap.softekmentalapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.fiap.softekmentalapp.util.Converters

@Entity(tableName = "assessment_results")
@TypeConverters(Converters::class)
data class AssessmentResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val responses: Map<Int, Int>,
    val score: Double,
    val classification: String
)