package com.example.phishing_detector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.phishing_detector.ui.theme.PhishingDetectorApp
import com.example.phishing_detector.ui.theme.PhishingDetectorTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var detector: PhishingDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detector = PhishingDetector(this)

        setContent {
            var modelLoaded by remember { mutableStateOf(false) }
            var modelError  by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    try { detector.loadModel(); modelLoaded = true }
                    catch (e: Exception) { modelError = e.message }
                }
            }

            PhishingDetectorTheme {
                PhishingDetectorApp(
                    detector    = detector,
                    modelLoaded = modelLoaded,
                    modelError  = modelError
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.close()
    }
}