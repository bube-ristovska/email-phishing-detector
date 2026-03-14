package com.example.phishing_detector.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val GmailRed     = Color(0xFFEA4335)
val GmailBlue    = Color(0xFF4285F4)
val GmailGreen   = Color(0xFF34A853)
val GmailYellow  = Color(0xFFFBBC04)
val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceGray  = Color(0xFFF6F8FC)
val DividerGray  = Color(0xFFE0E0E0)
val TextPrimary  = Color(0xFF202124)
val TextSecondary= Color(0xFF5F6368)
val TextTertiary = Color(0xFF80868B)
val UnreadBlue   = Color(0xFF1A73E8)
val DrawerBg     = Color(0xFFF6F8FC)

@Composable
fun PhishingDetectorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary      = GmailBlue,
            onPrimary    = Color.White,
            secondary    = GmailRed,
            background   = SurfaceGray,
            surface      = SurfaceWhite,
            onBackground = TextPrimary,
            onSurface    = TextPrimary,
            outline      = DividerGray
        ),
        content = content
    )
}