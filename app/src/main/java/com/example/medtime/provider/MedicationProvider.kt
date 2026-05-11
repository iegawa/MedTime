package com.example.medtime.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri

class MedicationProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.example.medtime.provider"
        private const val MEDICATIONS = 1
        private const val HISTORY = 2
        
        val CONTENT_URI_HISTORY: Uri = "content://$AUTHORITY/history".toUri()
        
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "medications", MEDICATIONS)
            addURI(AUTHORITY, "history", HISTORY)
        }
        
        // Mock storage for history in memory
        private val historyList = mutableListOf<ContentValues>()
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        return when (uriMatcher.match(uri)) {
            MEDICATIONS -> {
                val cursor = MatrixCursor(arrayOf("_id", "name", "dosage", "time"))
                cursor.addRow(arrayOf(1, "Paracetamol", "500mg", "08:00"))
                cursor.addRow(arrayOf(2, "Amoxicilina", "250mg", "14:00"))
                cursor.addRow(arrayOf(3, "Vitamina C", "1g", "20:00"))
                cursor
            }
            HISTORY -> {
                val cursor = MatrixCursor(arrayOf("_id", "medication_id", "timestamp", "status"))
                historyList.forEachIndexed { index, values ->
                    cursor.addRow(arrayOf(
                        index + 1,
                        values.getAsInteger("medication_id"),
                        values.getAsString("timestamp"),
                        values.getAsString("status")
                    ))
                }
                cursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            MEDICATIONS -> "vnd.android.cursor.dir/vnd.example.medication"
            HISTORY -> "vnd.android.cursor.dir/vnd.example.history"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) == HISTORY && values != null) {
            historyList.add(values)
            Log.d("MedicationProvider", "Histórico atualizado: $values")
            context?.contentResolver?.notifyChange(uri, null)
            return Uri.withAppendedPath(uri, historyList.size.toString())
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0
}
