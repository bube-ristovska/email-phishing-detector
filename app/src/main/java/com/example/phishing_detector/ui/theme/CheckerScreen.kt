package com.example.phishing_detector.ui.theme
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phishing_detector.PhishingDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckerScreen(
    detector: PhishingDetector,
    modelLoaded: Boolean,
    modelError: String?,
    onOpenDrawer: () -> Unit
) {
    var emailText  by remember { mutableStateOf("") }
    var scanResult by remember { mutableStateOf<Pair<Boolean, Float>?>(null) }
    var isScanning by remember { mutableStateOf(false) }
    val scope      = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Surface(shadowElevation = 2.dp, color = SurfaceWhite) {
                Row(
                    modifier          = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextSecondary)
                    }
                    Text(
                        "Email Checker",
                        fontWeight = FontWeight.Medium,
                        fontSize   = 18.sp,
                        color      = TextPrimary,
                        modifier   = Modifier.weight(1f)
                    )
                    if (!modelLoaded && modelError == null) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(20.dp).padding(end = 12.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        },
        containerColor = SurfaceGray
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            // Model status chip
            if (!modelLoaded) {
                val isError = modelError != null
                Surface(
                    shape    = RoundedCornerShape(50),
                    color    = if (isError) Color(0xFFFCE8E6) else Color(0xFFFFF3E0),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier          = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isError) "⚠️" else "⏳", fontSize = 13.sp)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            modelError?.let { "Model error: $it" } ?: "Loading detection model…",
                            fontSize = 12.sp,
                            color    = if (isError) GmailRed else Color(0xFF795548)
                        )
                    }
                }
            }

            // Input card
            Surface(
                modifier       = Modifier.fillMaxWidth(),
                shape          = RoundedCornerShape(16.dp),
                color          = SurfaceWhite,
                shadowElevation= 2.dp
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Email, contentDescription = null, tint = GmailBlue, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Paste Email Content", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value         = emailText,
                        onValueChange = { emailText = it; scanResult = null },
                        placeholder   = {
                            Text(
                                "Paste the full email text here to check if it's a phishing attempt…",
                                color    = TextTertiary,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(220.dp),
                        shape    = RoundedCornerShape(12.dp),
                        enabled  = !isScanning,
                        colors   = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = GmailBlue,
                            unfocusedBorderColor = DividerGray
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick  = { emailText = ""; scanResult = null },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape    = RoundedCornerShape(12.dp),
                            enabled  = emailText.isNotEmpty()
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Clear")
                        }
                        Button(
                            onClick  = {
                                if (emailText.isNotBlank() && modelLoaded && !isScanning) {
                                    isScanning = true
                                    scanResult = null
                                    scope.launch {
                                        delay(400)
                                        val result = withContext(Dispatchers.IO) { detector.predict(emailText) }
                                        scanResult = result
                                        isScanning = false
                                    }
                                }
                            },
                            modifier = Modifier.weight(2f).height(48.dp),
                            shape    = RoundedCornerShape(12.dp),
                            enabled  = modelLoaded && emailText.isNotBlank() && !isScanning,
                            colors   = ButtonDefaults.buttonColors(containerColor = GmailBlue)
                        ) {
                            if (isScanning) {
                                CircularProgressIndicator(
                                    modifier    = Modifier.size(18.dp),
                                    color       = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Scanning…")
                            } else {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Scan Email")
                            }
                        }
                    }
                }
            }

            // Result
            AnimatedVisibility(
                visible  = scanResult != null,
                enter    = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                modifier = Modifier.padding(top = 12.dp)
            ) {
                scanResult?.let { (isPhishing, confidence) ->
                    ScanResultCard(isPhishing = isPhishing, confidence = confidence)
                }
            }

            // Tips card
            Spacer(Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                color    = Color(0xFFF0F4FF),
                border   = BorderStroke(1.dp, GmailBlue.copy(alpha = 0.2f))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💡", fontSize = 16.sp)
                        Spacer(Modifier.width(8.dp))
                        Text("Phishing Tips", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = GmailBlue)
                    }
                    Spacer(Modifier.height(8.dp))
                    listOf(
                        "Check the sender's actual email domain carefully",
                        "Hover over links before clicking to see the real URL",
                        "Legitimate companies never ask for passwords via email",
                        "Look for urgency and fear-based language as red flags"
                    ).forEach { tip ->
                        Row(Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                            Text("•", color = GmailBlue, modifier = Modifier.padding(end = 6.dp, top = 1.dp))
                            Text(tip, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}