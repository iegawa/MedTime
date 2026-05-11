package com.example.medtime

package com.example.medtime

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medtime.service.MedicationReminderService
import com.example.medtime.ui.theme.MedTimeTheme
import com.example.medtime.viewmodel.MedicationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedTimeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MedicationViewModel = viewModel()) {
    val context = LocalContext.current
    val lastUpdate by viewModel.lastUpdate

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Iniciar o serviço foreground
        val intent = Intent(context, MedicationReminderService::class.java)
        context.startForegroundService(intent)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "MedTime - Lembrete de Medicamentos",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "O serviço de monitoramento está ativo.")
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Próxima dose pendente: Paracetamol 500mg",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.markAsTaken()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Check: Marcar como Tomado")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = lastUpdate,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(onClick = {
                // Simulação de disparo de alarme para teste
                val intent = Intent("com.example.medtime.ACTION_REMINDER_ALARM").apply {
                    setPackage(context.packageName)
                }
                context.sendBroadcast(intent)
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Simular Alarme de Notificação")
            }
        }
    }
}
