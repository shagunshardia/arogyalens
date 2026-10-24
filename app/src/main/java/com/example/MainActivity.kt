package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppLanguage
import com.example.data.ScanMode
import com.example.ui.components.ArogyaBottomBar
import com.example.ui.screens.AddRecordDialog
import com.example.ui.screens.CareScreen
import com.example.ui.screens.EmergencyDialog
import com.example.ui.screens.HealthWorkerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HowItWorksDialog
import com.example.ui.screens.LocationPickerDialog
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RecordsScreen
import com.example.ui.screens.ReferralQrDialog
import com.example.ui.screens.RegisterPatientDialog
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.VoiceAssistDialog
import com.example.ui.theme.ArogyaLensTheme
import com.example.ui.theme.MedicalCyan
import com.example.ui.theme.MedicalCyanLight
import com.example.ui.theme.MedicalTeal
import com.example.ui.theme.NavyDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppDestination
import com.example.viewmodel.ArogyaLensViewModel
import com.example.viewmodel.ArogyaUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArogyaLensTheme {
                val viewModel: ArogyaLensViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                ArogyaLensApp(viewModel = viewModel, uiState = uiState)
            }
        }
    }
}

@Composable
fun ArogyaLensApp(
    viewModel: ArogyaLensViewModel,
    uiState: ArogyaUiState
) {
    var showLanguageDialog by remember { mutableStateOf(false) }

    // Android Hardware Back button handling
    BackHandler(enabled = uiState.currentDestination != AppDestination.HOME) {
        if (uiState.currentDestination == AppDestination.HEALTH_WORKER) {
            viewModel.navigateTo(AppDestination.PROFILE)
        } else {
            viewModel.navigateTo(AppDestination.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (uiState.isOnboarded &&
                uiState.currentDestination != AppDestination.SCANNER &&
                uiState.currentDestination != AppDestination.HEALTH_WORKER &&
                uiState.currentDestination != AppDestination.ONBOARDING
            ) {
                ArogyaBottomBar(
                    currentDestination = uiState.currentDestination,
                    onNavigate = { dest -> viewModel.navigateTo(dest) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (uiState.currentDestination == AppDestination.SCANNER || uiState.currentDestination == AppDestination.HEALTH_WORKER) 0.dp else innerPadding.calculateBottomPadding())
        ) {
            Crossfade(
                targetState = uiState.currentDestination,
                label = "screen_transition"
            ) { destination ->
                when (destination) {
                    AppDestination.ONBOARDING -> {
                        OnboardingScreen(
                            currentLanguage = uiState.currentLanguage,
                            onLanguageChange = { viewModel.setLanguage(it) },
                            onContinue = { name, phone, loc ->
                                viewModel.completeOnboarding(name, phone, loc)
                            }
                        )
                    }
                    AppDestination.HOME -> {
                        HomeScreen(
                            uiState = uiState,
                            onNavigate = { dest -> viewModel.navigateTo(dest) },
                            onOpenScannerMode = { mode ->
                                viewModel.setScanMode(mode)
                                viewModel.navigateTo(AppDestination.SCANNER)
                            },
                            onLanguageClick = { showLanguageDialog = true },
                            onVoiceClick = { viewModel.setVoiceAssistOverlayVisible(true) },
                            onLocationClick = { viewModel.setLocationPickerVisible(true) },
                            onEmergencyClick = { viewModel.setEmergencyDialogVisible(true) },
                            onSyncClick = { viewModel.syncOfflineData() },
                            onSelectFacility = { fac -> viewModel.selectFacility(fac) }
                        )
                    }
                    AppDestination.CARE -> {
                        CareScreen(
                            uiState = uiState,
                            onFacilitySelect = { fac -> viewModel.selectFacility(fac) },
                            onSlotSelect = { slot -> viewModel.selectSlot(slot) },
                            onBookAppointment = { viewModel.bookAppointment() },
                            onMedicineQueryChange = { query -> viewModel.updateMedicineSearch(query) },
                            onShowQrDialog = { viewModel.setReferralQrDialogVisible(true) },
                            onLanguageClick = { showLanguageDialog = true },
                            onVoiceClick = { viewModel.setVoiceAssistOverlayVisible(true) },
                            onLocationClick = { viewModel.setLocationPickerVisible(true) }
                        )
                    }
                    AppDestination.SCANNER -> {
                        ScannerScreen(
                            uiState = uiState,
                            onBackClick = { viewModel.navigateTo(AppDestination.HOME) },
                            onModeChange = { mode -> viewModel.setScanMode(mode) },
                            onSelectSample = { idx -> viewModel.selectSampleScan(idx) },
                            onCapture = { viewModel.captureScan() },
                            onRetake = { viewModel.retakeScan() },
                            onAnalyze = { viewModel.runAiAnalysis() },
                            onContinueToCare = {
                                viewModel.addCurrentScanToRecords()
                                viewModel.navigateTo(AppDestination.CARE)
                            },
                            onSaveToRecords = {
                                viewModel.addCurrentScanToRecords()
                                viewModel.navigateTo(AppDestination.RECORDS)
                            },
                            onHowItWorksClick = { viewModel.setHowItWorksDialogVisible(true) }
                        )
                    }
                    AppDestination.RECORDS -> {
                        RecordsScreen(
                            uiState = uiState,
                            onFilterChange = { filter -> viewModel.setRecordsFilter(filter) },
                            onToggleFollowUp = { id -> viewModel.toggleFollowUpComplete(id) },
                            onAddRecordClick = { viewModel.setAddRecordDialogVisible(true) },
                            onShowQrClick = { viewModel.setReferralQrDialogVisible(true) },
                            onLanguageClick = { showLanguageDialog = true },
                            onVoiceClick = { viewModel.setVoiceAssistOverlayVisible(true) },
                            onLocationClick = { viewModel.setLocationPickerVisible(true) }
                        )
                    }
                    AppDestination.PROFILE -> {
                        ProfileScreen(
                            uiState = uiState,
                            onNavigateToWorkerMode = { viewModel.navigateTo(AppDestination.HEALTH_WORKER) },
                            onToggleVoice = { enabled -> viewModel.toggleVoiceAssistance(enabled) },
                            onToggleOffline = { viewModel.toggleOfflineMode() },
                            onSyncClick = { viewModel.syncOfflineData() },
                            onLanguageClick = { showLanguageDialog = true },
                            onVoiceClick = { viewModel.setVoiceAssistOverlayVisible(true) },
                            onLocationClick = { viewModel.setLocationPickerVisible(true) },
                            onShowQrDialog = { viewModel.setReferralQrDialogVisible(true) }
                        )
                    }
                    AppDestination.HEALTH_WORKER -> {
                        HealthWorkerScreen(
                            uiState = uiState,
                            onBackToPatientMode = { viewModel.navigateTo(AppDestination.PROFILE) },
                            onRegisterPatientClick = { viewModel.setRegisterPatientDialogVisible(true) },
                            onScanSymptomClick = {
                                viewModel.setScanMode(ScanMode.VISUAL_SYMPTOM)
                                viewModel.navigateTo(AppDestination.SCANNER)
                            },
                            onScanReportClick = {
                                viewModel.setScanMode(ScanMode.MEDICAL_REPORT)
                                viewModel.navigateTo(AppDestination.SCANNER)
                            },
                            onSyncClick = { viewModel.syncOfflineData() }
                        )
                    }
                }
            }

            // Sync Notification Toast
            AnimatedVisibility(
                visible = uiState.showSyncSuccessNotification,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp, start = 20.dp, end = 20.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SuccessGreen,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sync Complete • All records up to date",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    // App Modals & Dialogs
    if (uiState.showEmergencyDialog) {
        EmergencyDialog(
            onDismiss = { viewModel.setEmergencyDialogVisible(false) }
        )
    }

    if (uiState.showVoiceAssistOverlay) {
        VoiceAssistDialog(
            onDismiss = { viewModel.setVoiceAssistOverlayVisible(false) }
        )
    }

    if (uiState.showHowItWorksDialog) {
        HowItWorksDialog(
            onDismiss = { viewModel.setHowItWorksDialogVisible(false) }
        )
    }

    if (uiState.showReferralQrDialog && uiState.activeReferral != null) {
        ReferralQrDialog(
            referral = uiState.activeReferral,
            tokenNumber = uiState.activeToken?.tokenNumber ?: "A-42",
            onDismiss = { viewModel.setReferralQrDialogVisible(false) }
        )
    }

    if (uiState.showLocationPicker) {
        LocationPickerDialog(
            currentLocation = uiState.profile.location,
            onSelectLocation = { loc -> viewModel.updateLocation(loc) },
            onDismiss = { viewModel.setLocationPickerVisible(false) }
        )
    }

    if (uiState.showAddRecordDialog) {
        AddRecordDialog(
            onAdd = { title, cat, sum -> viewModel.addNewCustomRecord(title, cat, sum) },
            onDismiss = { viewModel.setAddRecordDialogVisible(false) }
        )
    }

    if (uiState.showRegisterPatientDialog) {
        RegisterPatientDialog(
            onRegister = { name, age, village, cond, risk ->
                viewModel.registerAshaPatient(name, age, village, cond, risk)
            },
            onDismiss = { viewModel.setRegisterPatientDialogVisible(false) }
        )
    }

    if (showLanguageDialog) {
        LanguagePickerDialog(
            currentLanguage = uiState.currentLanguage,
            onSelectLanguage = { lang ->
                viewModel.setLanguage(lang)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
}

@Composable
fun LanguagePickerDialog(
    currentLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌐 Select Language / भाषा",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppLanguage.values().forEach { lang ->
                        val isSelected = currentLanguage == lang
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MedicalCyanLight else SurfaceSubtle,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) MedicalCyan else SurfaceBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectLanguage(lang) }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = lang.displayName,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = NavyDark
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MedicalTeal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
