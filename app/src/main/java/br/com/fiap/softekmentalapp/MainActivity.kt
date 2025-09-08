package br.com.fiap.softekmentalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.com.fiap.softekmentalapp.data.ThemePreferences
import br.com.fiap.softekmentalapp.data.themeDataStore
import br.com.fiap.softekmentalapp.navigation.AppNavGraph
import br.com.fiap.softekmentalapp.repository.AssessmentRepository
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.ui.theme.SoftekMentalAppTheme
import br.com.fiap.softekmentalapp.workers.ReminderWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CheckinRepository.initialize(this)
        AssessmentRepository.initialize(this)
        setupReminderWorker()

        setContent {
            val themePreferences = remember { ThemePreferences(themeDataStore) }
            val isDarkTheme by themePreferences.isDarkTheme.collectAsState(initial = false)
            val coroutineScope = rememberCoroutineScope()

            SoftekMentalAppTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onThemeUpdated = {
                        coroutineScope.launch {
                            themePreferences.setDarkTheme(!isDarkTheme)
                        }
                    },
                    coroutineScope = coroutineScope
                )
            }
        }
    }

    private fun setupReminderWorker() {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            24, // Intervalo de repetição
            TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}