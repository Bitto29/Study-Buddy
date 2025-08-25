
package com.bitto.focusstudy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bitto.focusstudy.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusStudyApp()
        }
    }
}

@Composable
fun FocusStudyApp() {
    val nav: NavHostController = rememberNavController()
    MaterialTheme(colorScheme = darkColorScheme()) { // Dark by default
        Surface {
            NavHost(navController = nav, startDestination = "home") {
                composable("home") { HomeScreen(nav) }
                composable("alarm") { AlarmScreen() }
                composable("pomodoro") { PomodoroScreen() }
                composable("music") { MusicScreen() }
                composable("focus") { FocusModeScreen() }
                composable("about") { AboutScreen() }
            }
        }
    }
}

@Composable
fun HomeScreen(nav: NavHostController) {
    val ctx = LocalContext.current
    val items = listOf(
        "Set Study Alarm" to "alarm",
        "Pomodoro Timer" to "pomodoro",
        "Study Music" to "music",
        "Focus Mode (DND)" to "focus",
        "About" to "about"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "FocusStudy",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            "Modern, distraction-free study hub for teens.",
            style = MaterialTheme.typography.bodyLarge
        )
        items.forEach { (label, route) ->
            Button(
                onClick = { nav.navigate(route) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge
            ) { Text(label, modifier = Modifier.padding(4.dp)) }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "Created by Basit Iqbal Bitto",
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        TextButton(onClick = {
            val url = "https://bitto.pages.dev/"
            ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("bitto.pages.dev →")
        }
    }
}
