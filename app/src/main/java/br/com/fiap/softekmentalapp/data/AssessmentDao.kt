package br.com.fiap.softekmentalapp.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.softekmentalapp.model.AssessmentResult

@Dao
interface AssessmentDao {
    @Insert
    suspend fun insert(result: AssessmentResult)

    @Query("SELECT * FROM assessment_results")
    suspend fun getAllResults(): List<AssessmentResult>

    @Query("DELETE FROM assessment_results")
    suspend fun clearAllResults()
}