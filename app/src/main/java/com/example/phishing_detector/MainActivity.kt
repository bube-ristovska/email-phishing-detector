package com.example.phishing_detector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var detector: PhishingDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load model
        detector = PhishingDetector(this)

        setContent {
            MaterialTheme {
                PhishingDetectorScreen(detector)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.close()
    }
}

@Composable
fun PhishingDetectorScreen(detector: PhishingDetector) {
    var emailText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var resultColor by remember { mutableStateOf(Color.Black) }
    var isLoading by remember { mutableStateOf(false) }
    var modelLoaded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Load model when screen appears
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                detector.loadModel()
                modelLoaded = true
            } catch (e: Exception) {
                resultText = "Error loading model: ${e.message}"
                resultColor = Color.Red
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Phishing Email Detector",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it },
                label = { Text("Paste email text here...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10,
                enabled = !isLoading
            )

            Button(
                onClick = {
                    if (emailText.isNotEmpty() && modelLoaded) {
                        isLoading = true
                        scope.launch {
                            try {
                                val result = withContext(Dispatchers.IO) {
                                    detector.predict(emailText)
                                }
                                val (isPhishing, confidence) = result

                                if (isPhishing) {
                                    resultText = "⚠️ PHISHING DETECTED\nConfidence: ${String.format("%.1f", confidence * 100)}%"
                                    resultColor = Color.Red
                                } else {
                                    resultText = "✅ Email appears legitimate\nConfidence: ${String.format("%.1f", confidence * 100)}%"
                                    resultColor = Color.Green
                                }
                            } catch (e: Exception) {
                                resultText = "Error: ${e.message}"
                                resultColor = Color.Red
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = modelLoaded && !isLoading
            ) {
                if (isLoading) {
                    Text("Processing...")
                } else if (!modelLoaded) {
                    Text("Loading model...")
                } else {
                    Text("Check Email")
                }
            }

            if (resultText.isNotEmpty()) {
                Text(
                    text = resultText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = resultColor,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
}