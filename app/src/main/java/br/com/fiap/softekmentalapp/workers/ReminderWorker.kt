package br.com.fiap.softekmentalapp.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.fiap.softekmentalapp.R

class ReminderWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        createNotificationChannel()
        showNotification()
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Lembretes de Bem-Estar",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Lembretes para check-in emocional"
        }
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Lembrete de Bem-Estar")
            .setContentText("Como você está se sentindo hoje? Faça seu check-in emocional!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    companion object {
        const val CHANNEL_ID = "reminder_channel"
    }
}