
package com.bitto.focusstudy.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bitto.focusstudy.alarm.AlarmReceiver
import java.util.concurrent.TimeUnit

@Composable
fun AlarmScreen() {
    val ctx = LocalContext.current
    var minutes by remember { mutableStateOf(1) }
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Study Alarm", style = MaterialTheme.typography.headlineMedium)
        Text("Tip: The alarm will only stop when your camera sees a book (we detect printed text).")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { if (minutes > 1) minutes-- }) { Text("-") }
            Text("$minutes min")
            Button(onClick = { minutes++ }) { Text("+") }
        }
        Button(
            onClick = { scheduleAlarm(ctx, minutes) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) { Text("Set Alarm in $minutes minute(s)") }
    }
}

private fun scheduleAlarm(ctx: Context, minutes: Int) {
    val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(ctx, AlarmReceiver::class.java)
    val pi = PendingIntent.getBroadcast(ctx, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    val triggerAt = SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(minutes.toLong())
    am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pi)
    Toast.makeText(ctx, "Alarm set for $minutes minute(s).", Toast.LENGTH_SHORT).show()
}
