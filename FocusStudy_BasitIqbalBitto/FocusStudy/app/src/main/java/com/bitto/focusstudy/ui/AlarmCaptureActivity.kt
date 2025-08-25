
package com.bitto.focusstudy.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.bitto.focusstudy.alarm.AlarmService
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.atomic.AtomicInt

class AlarmCaptureActivity : ComponentActivity() {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (!granted) finish()
        else startService(Intent(this, AlarmService::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AlarmCaptureScreen() }
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    @Composable
    fun AlarmCaptureScreen() {
        val ctx = LocalContext.current
        var detected by remember { mutableStateOf(false) }
        LaunchedEffect(detected) {
            if (detected) {
                stopService(Intent(ctx, AlarmService::class.java))
                finish()
            }
        }
        Surface {
            Column(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Show a book to the camera to stop the alarm.", style = MaterialTheme.typography.headlineSmall)
                AndroidView(factory = { context ->
                    val previewView = PreviewView(context)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val analyzer = ImageAnalysis.Builder().build().also {
                            it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy: ImageProxy ->
                                processImageProxy(imageProxy) { ok ->
                                    if (ok) detected = true
                                }
                            }
                        }
                        val selector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(this, selector, preview, analyzer)
                    }, ContextCompat.getMainExecutor(context))
                    previewView
                }, modifier = Modifier.weight(1f))
                Text("Tip: Hold a page close so printed text fills the frame.")
                Button(onClick = { /* fallback */ stopService(Intent(ctx, AlarmService::class.java)); finish() },
                    modifier = Modifier.fillMaxWidth()) { Text("I don't have a book (Stop)") }
            }
        }
    }

    private fun processImageProxy(imageProxy: ImageProxy, onResult: (Boolean) -> Unit) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val lineCount = visionText.textBlocks.sumOf { block -> block.lines.size }
                    // Heuristic: if many lines of text are detected, we assume it's a book/page
                    onResult(lineCount >= 6)
                }
                .addOnFailureListener {
                    onResult(false)
                }
                .addOnCompleteListener { imageProxy.close() }
        } else {
            imageProxy.close()
            onResult(false)
        }
    }

    override fun onStart() {
        super.onStart()
        startService(Intent(this, AlarmService::class.java))
    }

    override fun onStop() {
        super.onStop()
    }
}
