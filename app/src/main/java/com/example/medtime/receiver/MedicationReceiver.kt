package com.example.medtime.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.medtime.service.MedicationReminderService

class MedicationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_REMINDER_ALARM = "com.example.medtime.ACTION_REMINDER_ALARM"
        const val NOTIFICATION_ID = 100
        const val CHANNEL_ID = "MedicationAlarmChannel"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                // Reiniciar o serviço para reagendar os alarmes
                val serviceIntent = Intent(context, MedicationReminderService::class.java)
                context.startForegroundService(serviceIntent)
            }
            ACTION_REMINDER_ALARM -> {
                showNotification(context)
            }
        }
    }

    private fun showNotification(context: Context) {
        // TO DO
}
