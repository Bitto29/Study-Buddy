
package com.bitto.focusstudy.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun FocusModeScreen() {
    val ctx = LocalContext.current
    val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val granted = nm.isNotificationPolicyAccessGranted
    val enabled = remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Focus Mode", style = MaterialTheme.typography.headlineMedium)
        Text("Enable Do Not Disturb during study sessions. You may need to grant permission.")
        if (!granted) {
            Button(onClick = {
                ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            }) { Text("Grant DND Permission") }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                    enabled.value = true
                }) { Text("Enable DND") }
                Button(onClick = {
                    nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                    enabled.value = false
                }) { Text("Disable DND") }
            }
            Text(if (enabled.value) "DND is ON" else "DND is OFF")
        }
        Text("Tip: Pair this with Pomodoro or set a Study Alarm for best results.")
    }
}
