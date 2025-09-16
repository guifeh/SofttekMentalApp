package br.com.fiap.softekmentalapp.repository

import android.content.Context
import br.com.fiap.softekmentalapp.model.AssessmentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AssessmentRepository {
    private lateinit var assessmentDao: AssessmentDao

    fun initialize(context: Context) {
        assessmentDao = AppDatabase.getDatabase(context).assessmentDao()
    }

    suspend fun addResult(result: AssessmentResult) {
        withContext(Dispatchers.IO) {
            assessmentDao.insert(result)
        }
    }

    suspend fun getAllResults(): List<AssessmentResult> {
        return withContext(Dispatchers.IO) {
            assessmentDao.getAllResults()
        }
    }

    suspend fun clearAllResults() {
        withContext(Dispatchers.IO) {
            assessmentDao.clearAllResults()
        }
    }
}