
package com.bitto.focusstudy.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.bitto.focusstudy.R

@Composable
fun AboutScreen() {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("About", style = MaterialTheme.typography.headlineMedium)
        Text("FocusStudy helps you build focus with alarms that only stop when your camera sees a real book, a Pomodoro timer with streaks/XP, and a simple music player.")
        AndroidView(factory = { ctx ->
            TextView(ctx).apply {
                setText(R.string.creator_credit_html)
                movementMethod = LinkMovementMethod.getInstance()
            }
        })
        Text(
            "Dark theme is default. Light theme is available via system settings.",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
