
package com.bitto.focusstudy.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.max

@Composable
fun PomodoroScreen() {
    var focus by remember { mutableStateOf(25) }
    var breakMin by remember { mutableStateOf(5) }
    var secondsLeft by remember { mutableStateOf(focus * 60) }
    var running by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf("Focus") }
    var streak by remember { mutableStateOf(0) }
    var xp by remember { mutableStateOf(0) }

    LaunchedEffect(running, phase) {
        while (running) {
            if (secondsLeft <= 0) {
                if (phase == "Focus") {
                    phase = "Break"
                    secondsLeft = breakMin * 60
                    streak += 1
                    xp += 10
                } else {
                    phase = "Focus"
                    secondsLeft = focus * 60
                }
            } else {
                delay(1000)
                secondsLeft = max(0, secondsLeft - 1)
            }
        }
    }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Pomodoro", style = MaterialTheme.typography.headlineMedium)
        Text("Phase: $phase")
        Text("Time: ${secondsLeft/60}:${(secondsLeft%60).toString().padStart(2,'0')}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { running = true }) { Text("Start") }
            Button(onClick = { running = false }) { Text("Pause") }
            Button(onClick = {
                running = false
                phase = "Focus"
                secondsLeft = focus * 60
            }) { Text("Reset") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Focus (min)"); Button({ if (focus>5){ focus--; if(phase=="Focus") secondsLeft=focus*60 } }){ Text("-") }
            Text("$focus"); Button({ focus++; if(phase=="Focus") secondsLeft=focus*60 }){ Text("+") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Break (min)"); Button({ if (breakMin>1){ breakMin-- } }){ Text("-") }
            Text("$breakMin"); Button({ breakMin++ }){ Text("+") }
        }
        Divider()
        Text("Streak: $streak  •  XP: $xp")
        LinearProgressIndicator(progress = (secondsLeft.toFloat())/((if (phase=="Focus") focus else breakMin)*60f))
        Text("Tip: Earn XP each completed focus session. Keep your streak going!")
    }
}
