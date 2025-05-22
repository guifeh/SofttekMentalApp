package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.AssessmentResult

object AssessmentRepository {
    private val results = mutableListOf<AssessmentResult>()

    fun addResult(result: AssessmentResult) {
        results.add(result)
    }

    fun getAllResults(): List<AssessmentResult> {
        return results
    }

    fun clearAllResults() {
        results.clear()
    }
}
