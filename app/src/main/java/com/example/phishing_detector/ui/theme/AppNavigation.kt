package com.example.phishing_detector.ui.theme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phishing_detector.PhishingDetector
import kotlinx.coroutines.launch

enum class Screen { INBOX, CHECKER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhishingDetectorApp(detector: PhishingDetector, modelLoaded: Boolean, modelError: String?) {
    val drawerState   = rememberDrawerState(DrawerValue.Closed)
    val scope         = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(Screen.INBOX) }

    ModalNavigationDrawer(
        drawerState   = drawerState,
        drawerContent = {
            SideDrawer(
                currentScreen = currentScreen,
                onNavigate    = { screen ->
                    currentScreen = screen
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        when (currentScreen) {
            Screen.INBOX -> InboxScreen(
                detector     = detector,
                modelLoaded  = modelLoaded,
                onOpenDrawer = { scope.launch { drawerState.open() } },
                onGoChecker  = { currentScreen = Screen.CHECKER }
            )
            Screen.CHECKER -> CheckerScreen(
                detector     = detector,
                modelLoaded  = modelLoaded,
                modelError   = modelError,
                onOpenDrawer = { scope.launch { drawerState.open() } }
            )
        }
    }
}

@Composable
fun SideDrawer(currentScreen: Screen, onNavigate: (Screen) -> Unit) {
    ModalDrawerSheet(
        drawerContainerColor = DrawerBg,
        modifier = Modifier.width(300.dp)
    ) {
        // Header
        Row(
            modifier          = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarCircle(initial = "PG", color = GmailRed, size = 36)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("PhishGuard", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Text("Email Security", fontSize = 12.sp, color = TextSecondary)
            }
        }

        HorizontalDivider(color = DividerGray)
        Spacer(Modifier.height(8.dp))

        DrawerItem(
            icon     = Icons.Outlined.ArrowForward,
            label    = "Demo Inbox",
            selected = currentScreen == Screen.INBOX,
            badge    = "5",
            onClick  = { onNavigate(Screen.INBOX) }
        )
        DrawerItem(
            icon     = Icons.Outlined.Search,
            label    = "Email Checker",
            selected = currentScreen == Screen.CHECKER,
            onClick  = { onNavigate(Screen.CHECKER) }
        )

        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = DividerGray)
        Spacer(Modifier.height(8.dp))

        Text(
            "  Labels",
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color      = TextTertiary,
            modifier   = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        DrawerItem(
            icon       = Icons.Outlined.Warning,
            label      = "Phishing",
            selected   = false,
            labelColor = GmailRed,
            onClick    = {}
        )
        DrawerItem(
            icon       = Icons.Outlined.Check,
            label      = "Legitimate",
            selected   = false,
            labelColor = GmailGreen,
            onClick    = {}
        )

    }
}