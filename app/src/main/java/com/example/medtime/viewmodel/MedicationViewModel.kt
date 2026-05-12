package com.example.medtime.viewmodel

import android.app.Application
import android.content.ContentValues
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MedicationViewModel(application: Application) : AndroidViewModel(application) {

    private val _lastUpdate = mutableStateOf("Nenhuma dose registrada hoje")
    val lastUpdate: State<String> = _lastUpdate

    fun markAsTaken() {
        val context = getApplication<Application>().applicationContext
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val values = ContentValues().apply {
            put("medication_id", 1) // ID mockado do Paracetamol
            put("timestamp", timestamp)
            put("status", "TAKEN")
        }

        val historyUri = "content://com.example.medtime.provider/history".toUri()
        val uri = context.contentResolver.insert(historyUri, values)

        if (uri != null) {
            _lastUpdate.value = "Dose de Paracetamol marcada como tomada às $timestamp"
        }
    }
}
