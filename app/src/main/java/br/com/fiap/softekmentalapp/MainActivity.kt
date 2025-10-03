package br.com.fiap.softekmentalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.com.fiap.softekmentalapp.navigation.AppNavGraph
import br.com.fiap.softekmentalapp.repository.AuthRepository
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.ui.theme.SoftekMentalAppTheme
import br.com.fiap.softekmentalapp.workers.ReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authRepository = AuthRepository()
        val checkinRepository = CheckinRepository()

        setupReminderWorker()

        setContent {
            val navController = rememberNavController()

            var isDarkTheme by remember { mutableStateOf(false) }

            SoftekMentalAppTheme(darkTheme = isDarkTheme) {
                AppNavGraph(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onThemeUpdated = { isDarkTheme = !isDarkTheme },
                    checkinRepository = checkinRepository,
                    authRepository = authRepository
                )
            }
        }
    }

    private fun setupReminderWorker() {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
