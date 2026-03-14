package com.example.phishing_detector.ui.theme
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phishing_detector.PhishingDetector
import com.example.phishing_detector.data.SampleEmail
import com.example.phishing_detector.data.sampleEmails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    detector: PhishingDetector,
    modelLoaded: Boolean,
    onOpenDrawer: () -> Unit,
    onGoChecker: () -> Unit
) {
    var selectedEmail by remember { mutableStateOf<SampleEmail?>(null) }
    var scanResult    by remember { mutableStateOf<Pair<Boolean, Float>?>(null) }
    var isScanning    by remember { mutableStateOf(false) }
    val scope         = rememberCoroutineScope()

    if (selectedEmail != null) {
        EmailDetailScreen(
            email       = selectedEmail!!,
            scanResult  = scanResult,
            isScanning  = isScanning,
            modelLoaded = modelLoaded,
            onBack      = { selectedEmail = null; scanResult = null },
            onScan      = {
                if (modelLoaded && !isScanning) {
                    isScanning = true
                    scope.launch {
                        delay(600)
                        val result = withContext(Dispatchers.IO) { detector.predict(selectedEmail!!.body) }
                        scanResult = result
                        isScanning = false
                    }
                }
            }
        )
        return
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 2.dp, color = SurfaceWhite) {
                Row(
                    modifier          = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextSecondary)
                    }
                    Text(
                        "Inbox",
                        fontWeight = FontWeight.Medium,
                        fontSize   = 20.sp,
                        color      = TextPrimary,
                        modifier   = Modifier.weight(1f)
                    )
                    AvatarCircle(initial = "U", color = GmailRed, size = 32)
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick        = onGoChecker,
                containerColor = GmailBlue,
                contentColor   = Color.White,
                icon           = { Icon(Icons.Default.Edit, contentDescription = null) },
                text           = { Text("Check Email") }
            )
        },
        containerColor = SurfaceGray
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top    = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 80.dp
            )
        ) {
            item {
                Text(
                    "  Sample phishing emails — tap to analyze",
                    fontSize = 11.sp,
                    color    = TextTertiary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
            items(sampleEmails) { email ->
                EmailRow(email = email, onClick = { selectedEmail = email; scanResult = null })
            }
        }
    }
}

@Composable
fun EmailRow(email: SampleEmail, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), color = SurfaceWhite) {
        Column {
            Row(
                modifier          = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarCircle(initial = email.senderInitial, color = email.avatarColor, size = 40)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            email.sender,
                            fontWeight = if (!email.isRead) FontWeight.Bold else FontWeight.Medium,
                            fontSize   = 14.sp,
                            color      = TextPrimary,
                            modifier   = Modifier.weight(1f)
                        )
                        Text(
                            email.time,
                            fontSize = 12.sp,
                            color    = if (!email.isRead) UnreadBlue else TextTertiary
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        email.subject,
                        fontWeight = if (!email.isRead) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize   = 13.sp,
                        color      = if (!email.isRead) TextPrimary else TextSecondary,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                    Text(
                        email.preview,
                        fontSize = 12.sp,
                        color    = TextTertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(start = 68.dp), color = DividerGray, thickness = 0.5.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailDetailScreen(
    email: SampleEmail,
    scanResult: Pair<Boolean, Float>?,
    isScanning: Boolean,
    modelLoaded: Boolean,
    onBack: () -> Unit,
    onScan: () -> Unit
) {
    Scaffold(
        topBar = {
            Surface(shadowElevation = 2.dp, color = SurfaceWhite) {
                Row(
                    modifier          = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextSecondary)
                    }
                    Text("Email", fontWeight = FontWeight.Medium, fontSize = 18.sp, color = TextPrimary)
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
        ) {
            // Email card
            Surface(
                modifier       = Modifier.fillMaxWidth().padding(12.dp),
                shape          = RoundedCornerShape(16.dp),
                color          = SurfaceWhite,
                shadowElevation= 2.dp
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(email.subject, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AvatarCircle(initial = email.senderInitial, color = email.avatarColor, size = 36)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(email.sender, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                            Text("to me", fontSize = 12.sp, color = TextTertiary)
                        }
                        Spacer(Modifier.weight(1f))
                        Text(email.time, fontSize = 12.sp, color = TextTertiary)
                    }
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = DividerGray)
                    Spacer(Modifier.height(16.dp))
                    Text(email.body, fontSize = 14.sp, color = TextPrimary, lineHeight = 22.sp)
                }
            }

            // Scan result (animated)
            AnimatedVisibility(
                visible = scanResult != null,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                modifier= Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                scanResult?.let { (isPhishing, confidence) ->
                    ScanResultCard(isPhishing = isPhishing, confidence = confidence)
                }
            }

            // Scan button
            if (scanResult == null) {
                Button(
                    onClick  = onScan,
                    enabled  = modelLoaded && !isScanning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .height(52.dp),
                    shape  = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GmailBlue)
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(Modifier.width(10.dp))
                        Text("Analyzing…")
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Scan for Phishing", fontSize = 15.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}