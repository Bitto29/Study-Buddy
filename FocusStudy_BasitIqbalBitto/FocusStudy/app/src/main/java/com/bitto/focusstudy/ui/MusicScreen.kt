
package com.bitto.focusstudy.ui

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun MusicScreen() {
    val ctx = LocalContext.current
    var playing by remember { mutableStateOf(false) }
    val player = remember {
        ExoPlayer.Builder(ctx).build()
    }
    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Study Music", style = MaterialTheme.typography.headlineMedium)
        Text("Plays a gentle system tone in loop. You can also add your own files via the ⋮ menu (not included here).")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                player.setMediaItem(MediaItem.fromUri(uri))
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL)
                player.prepare()
                player.play()
                playing = true
            }) { Text("Play") }
            Button(onClick = { player.pause(); playing = false }) { Text("Pause") }
        }
        Text(if (playing) "Playing…" else "Paused")
    }
}
