package br.com.fiap.softekmentalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.com.fiap.softekmentalapp.navigation.AppNavGraph
import br.com.fiap.softekmentalapp.repository.AssessmentRepository
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.ui.theme.SoftekMentalAppTheme
import br.com.fiap.softekmentalapp.workers.ReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CheckinRepository.initialize(this)
        AssessmentRepository.initialize(this)
        setContent {
            SoftekMentalAppTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}