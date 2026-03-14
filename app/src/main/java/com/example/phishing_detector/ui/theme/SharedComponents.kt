package com.example.phishing_detector.ui.theme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScanResultCard(isPhishing: Boolean, confidence: Float) {
    val bgColor     = if (isPhishing) Color(0xFFFCE8E6) else Color(0xFFE6F4EA)
    val accentColor = if (isPhishing) GmailRed else GmailGreen
    val headline    = if (isPhishing) "Phishing Detected" else "Looks Legitimate"
    val subtitle    = if (isPhishing)
        "This email shows signs of a phishing attack. Do not click any links or provide any personal information."
    else
        "No phishing patterns detected. This email appears to be safe."

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        shape          = RoundedCornerShape(16.dp),
        color          = bgColor
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (isPhishing) "⚠️" else "✅",
                    fontSize = 22.sp
                )
                Spacer(Modifier.width(10.dp))
                Text(headline, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = accentColor)
            }
            Spacer(Modifier.height(8.dp))
            Text(subtitle, fontSize = 13.sp, color = accentColor.copy(alpha = 0.85f), lineHeight = 18.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                "Confidence: ${String.format("%.1f", confidence * 100)}%",
                fontSize   = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color      = accentColor
            )
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress    = confidence,
                modifier    = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)),
                color       = accentColor,
                trackColor  = accentColor.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    badge: String? = null,
    labelColor: Color = TextPrimary,
    onClick: () -> Unit
) {
    val bg = if (selected) GmailBlue.copy(alpha = 0.12f) else Color.Transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(50))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint     = if (selected) GmailBlue else labelColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            label,
            color      = if (selected) GmailBlue else labelColor,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            modifier   = Modifier.weight(1f)
        )
        if (badge != null) {
            Text(badge, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AvatarCircle(initial: String, color: Color, size: Int = 40) {
    Box(
        modifier         = Modifier.size(size.dp).clip(CircleShape).background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(initial, color = Color.White, fontSize = (size / 2.5).sp, fontWeight = FontWeight.Bold)
    }
}