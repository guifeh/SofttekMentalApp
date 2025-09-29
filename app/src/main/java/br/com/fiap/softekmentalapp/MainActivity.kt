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
import br.com.fiap.softekmentalapp.repository.AuthRepository
import br.com.fiap.softekmentalapp.repository.CheckinRepository
import br.com.fiap.softekmentalapp.ui.theme.SoftekMentalAppTheme
import br.com.fiap.softekmentalapp.workers.ReminderWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repositório de autenticação
        val authRepository = AuthRepository()

        // Função que devolve o token atual
        val tokenProvider: () -> String? = { authRepository.getToken() }

        // Repositório de checkins
        val checkinApiService = CheckinApiService.create()
        val checkinRepository = CheckinRepository(checkinApiService, tokenProvider)

        setupReminderWorker()

        setContent {
            val themePreferences = remember { ThemePreferences(themeDataStore) }
            val isDarkTheme by themePreferences.isDarkTheme.collectAsState(initial = false)
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()

            SoftekMentalAppTheme(darkTheme = isDarkTheme) {
                AppNavGraph(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onThemeUpdated = {
                        coroutineScope.launch {
                            themePreferences.setDarkTheme(!isDarkTheme)
                        }
                    },
                    checkinRepository = checkinRepository
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

