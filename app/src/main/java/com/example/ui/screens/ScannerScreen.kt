package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LabStatus
import com.example.data.RiskLevel
import com.example.data.ScanMode
import com.example.ui.components.RiskBadge
import com.example.ui.theme.AlertWarning
import com.example.ui.theme.AlertWarningBg
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedLight
import com.example.ui.theme.MedicalCyan
import com.example.ui.theme.MedicalCyanLight
import com.example.ui.theme.MedicalTeal
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyMedium
import com.example.ui.theme.SaffronIndia
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceFrosted
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppDestination
import com.example.viewmodel.ArogyaUiState
import com.example.viewmodel.ScanProcessState

@Composable
fun ScannerScreen(
    uiState: ArogyaUiState,
    onBackClick: () -> Unit,
    onModeChange: (ScanMode) -> Unit,
    onSelectSample: (Int) -> Unit,
    onCapture: () -> Unit,
    onRetake: () -> Unit,
    onAnalyze: () -> Unit,
    onContinueToCare: () -> Unit,
    onSaveToRecords: () -> Unit,
    onHowItWorksClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Scanner Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "ArogyaLens Scanner",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(
                onClick = onHowItWorksClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .testTag("scanner_how_it_works_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "How it works",
                    tint = Color.White
                )
            }
        }

        // Scan Mode Tabs (Visual Symptom vs Medical Report)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (uiState.scanMode == ScanMode.VISUAL_SYMPTOM) MedicalCyan else Color.Transparent)
                    .clickable { onModeChange(ScanMode.VISUAL_SYMPTOM) }
                    .padding(vertical = 10.dp)
                    .testTag("tab_mode_symptom"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        tint = if (uiState.scanMode == ScanMode.VISUAL_SYMPTOM) NavyDark else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Visual Symptom",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.scanMode == ScanMode.VISUAL_SYMPTOM) NavyDark else Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (uiState.scanMode == ScanMode.MEDICAL_REPORT) MedicalCyan else Color.Transparent)
                    .clickable { onModeChange(ScanMode.MEDICAL_REPORT) }
                    .padding(vertical = 10.dp)
                    .testTag("tab_mode_report"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = if (uiState.scanMode == ScanMode.MEDICAL_REPORT) NavyDark else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Medical Report",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.scanMode == ScanMode.MEDICAL_REPORT) NavyDark else Color.White
                    )
                }
            }
        }

        // Main Content Area based on scan state
        when (uiState.scanProcessState) {
            ScanProcessState.IDLE -> {
                CameraViewPort(
                    scanMode = uiState.scanMode,
                    selectedIndex = uiState.selectedSampleIndex,
                    onSelectSample = onSelectSample,
                    onCapture = onCapture,
                    modifier = Modifier.weight(1f)
                )
            }
            ScanProcessState.PREVIEW_CAPTURED -> {
                ReviewCapturedPreview(
                    scanMode = uiState.scanMode,
                    selectedIndex = uiState.selectedSampleIndex,
                    onRetake = onRetake,
                    onAnalyze = onAnalyze,
                    modifier = Modifier.weight(1f)
                )
            }
            ScanProcessState.ANALYZING -> {
                AnalyzingAiState(
                    currentStep = uiState.analysisStep,
                    modifier = Modifier.weight(1f)
                )
            }
            ScanProcessState.ANALYSIS_COMPLETE -> {
                if (uiState.scanMode == ScanMode.VISUAL_SYMPTOM && uiState.currentVisualResult != null) {
                    VisualAssessmentResult(
                        result = uiState.currentVisualResult,
                        onContinueToCare = onContinueToCare,
                        onSaveToRecords = onSaveToRecords,
                        onScanAgain = onRetake,
                        modifier = Modifier.weight(1f)
                    )
                } else if (uiState.currentReportResult != null) {
                    ReportAssessmentResult(
                        report = uiState.currentReportResult,
                        onContinueToCare = onContinueToCare,
                        onSaveToRecords = onSaveToRecords,
                        onScanAgain = onRetake,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CameraViewPort(
    scanMode: ScanMode,
    selectedIndex: Int,
    onSelectSample: (Int) -> Unit,
    onCapture: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_laser")
    val laserOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 280f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_y"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sample condition picker (for interactive testing)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (scanMode == ScanMode.VISUAL_SYMPTOM) "Choose Symptom Sample to Simulate:" else "Choose Document Sample:",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (scanMode == ScanMode.VISUAL_SYMPTOM) {
                    items(3) { index ->
                        val titles = listOf("🔴 Skin Irritation (Moderate)", "👁 Mild Conjunctivitis (Low)", "⚠️ Diabetic Ulcer (High)")
                        val isSel = selectedIndex == index
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) MedicalCyan else Color(0x22FFFFFF),
                            modifier = Modifier
                                .clickable { onSelectSample(index) }
                                .testTag("sample_symptom_$index")
                        ) {
                            Text(
                                text = titles[index],
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) NavyDark else Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                } else {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MedicalCyan,
                            modifier = Modifier.testTag("sample_report_0")
                        ) {
                            Text(
                                text = "📄 CBC Blood Report (Hb 10.2)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDark,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Scanner Viewfinder Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0F1E2E))
                .border(2.dp, MedicalCyan.copy(alpha = 0.6f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Corner Bracket Guides
            CornerBracketsOverlay()

            // Simulated Symptom / Document graphic in background
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            if (scanMode == ScanMode.VISUAL_SYMPTOM) {
                                when (selectedIndex) {
                                    0 -> Color(0xFFF4A261).copy(alpha = 0.25f)
                                    1 -> Color(0xFF2A9D8F).copy(alpha = 0.25f)
                                    else -> Color(0xFFE63946).copy(alpha = 0.25f)
                                }
                            } else Color(0xFF00B4D8).copy(alpha = 0.25f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (scanMode == ScanMode.VISUAL_SYMPTOM) Icons.Default.QrCodeScanner else Icons.Default.Description,
                        contentDescription = null,
                        tint = MedicalCyan,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (scanMode == ScanMode.VISUAL_SYMPTOM)
                        "Place affected area inside the frame"
                    else
                        "Align document within borders",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Make sure the area is well-lit and in focus",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            // Laser Line Scan Animation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .offset(y = (laserOffset - 140).dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, MedicalCyan, Color.Transparent)
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Privacy & Info indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Images processed securely on-device • No cloud storage required",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Camera Controls (Gallery, Shutter Button, Switch Camera)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gallery Picker
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onCapture() }
                    .testTag("scanner_gallery_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Gallery",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Gallery", fontSize = 11.sp, color = Color.White)
            }

            // Center Large Shutter Button
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(MedicalCyan)
                    .clickable { onCapture() }
                    .testTag("scanner_shutter_btn"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }

            // Camera Switch
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { }
                    .testTag("scanner_flip_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch Camera",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Flip", fontSize = 11.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun CornerBracketsOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Left
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(24.dp)
                .border(2.dp, MedicalCyan, RoundedCornerShape(topStart = 8.dp))
        )
        // Top Right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .border(2.dp, MedicalCyan, RoundedCornerShape(topEnd = 8.dp))
        )
        // Bottom Left
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(24.dp)
                .border(2.dp, MedicalCyan, RoundedCornerShape(bottomStart = 8.dp))
        )
        // Bottom Right
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(24.dp)
                .border(2.dp, MedicalCyan, RoundedCornerShape(bottomEnd = 8.dp))
        )
    }
}

@Composable
fun ReviewCapturedPreview(
    scanMode: ScanMode,
    selectedIndex: Int,
    onRetake: () -> Unit,
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Captured Image Ready",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Verify clarity before proceeding with AI analysis",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // Preview box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF14293D))
                .border(2.dp, MedicalCyan, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (scanMode == ScanMode.VISUAL_SYMPTOM) "Visible Symptom Sample #$selectedIndex" else "Lab Test Document",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "High Clarity • Good Illumination",
                    color = MedicalCyan,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Button(
                onClick = onRetake,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("scanner_retake_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Retake", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = onAnalyze,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("scanner_analyze_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = NavyDark)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Analyze AI", color = NavyDark, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
fun AnalyzingAiState(
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MedicalCyan,
            strokeWidth = 4.dp,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "ArogyaLens AI is analyzing...",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Offline On-Device Neural Engine",
            fontSize = 12.sp,
            color = MedicalCyan,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Step checklist
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AnalysisStepRow(
                label = "Checking visual patterns & textures",
                isDone = currentStep >= 1,
                isCurrent = currentStep == 1
            )
            AnalysisStepRow(
                label = "Comparing symptoms with public health database",
                isDone = currentStep >= 2,
                isCurrent = currentStep == 2
            )
            AnalysisStepRow(
                label = "Calculating health risk & triage priority",
                isDone = currentStep >= 3,
                isCurrent = currentStep == 3
            )
        }
    }
}

@Composable
fun AnalysisStepRow(
    label: String,
    isDone: Boolean,
    isCurrent: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDone) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(20.dp)
            )
        } else if (isCurrent) {
            CircularProgressIndicator(
                color = MedicalCyan,
                strokeWidth = 2.dp,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isDone || isCurrent) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isDone) Color.White else if (isCurrent) MedicalCyan else Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun VisualAssessmentResult(
    result: com.example.data.ScanResult,
    onContinueToCare: () -> Unit,
    onSaveToRecords: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AI Assessment Result",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = NavyDark
            )
            RiskBadge(riskLevel = result.riskLevel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Condition & Confidence Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceSubtle,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Possible Condition",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = result.conditionName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Confidence: ${result.confidenceScore}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MedicalTeal
                    )
                    Text(
                        text = result.timestamp,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description & Key Observations
        Text(
            text = "Observations",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = NavyDark
        )
        Text(
            text = result.description,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            result.keyObservations.forEach { obs ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("• ", color = MedicalTeal, fontWeight = FontWeight.Bold)
                    Text(
                        text = obs,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Care Guidance Box
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (result.riskLevel == RiskLevel.HIGH) EmergencyRedLight else MedicalCyanLight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Recommended Care Guidance:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (result.riskLevel == RiskLevel.HIGH) EmergencyRed else NavyDark
                )
                Text(
                    text = result.careGuidance,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Medical Disclaimer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceSubtle)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "⚠️ This is an AI-assisted screening result, not a definitive medical diagnosis. Consult a medical professional at your local PHC.",
                fontSize = 10.sp,
                color = TextSecondary,
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action buttons
        Button(
            onClick = onContinueToCare,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("btn_continue_to_care"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Continue to Care Guidance", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onSaveToRecords,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_save_to_records"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceSubtle)
            ) {
                Text("Save to Records", color = NavyDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Button(
                onClick = onScanAgain,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceSubtle)
            ) {
                Text("Scan Another", color = NavyDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ReportAssessmentResult(
    report: com.example.data.MedicalReportResult,
    onContinueToCare: () -> Unit,
    onSaveToRecords: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Medical Report Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = NavyDark
                )
                Text(
                    text = "${report.testTitle} • ${report.testDate}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            RiskBadge(riskLevel = report.riskLevel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Extracted Lab Values",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = NavyDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lab values table
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceSubtle)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            report.labValues.forEach { lab ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = lab.parameter,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NavyDark
                        )
                        Text(
                            text = "Normal: ${lab.normalRange}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = lab.resultValue,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = when (lab.status) {
                                LabStatus.NORMAL -> SuccessGreen
                                LabStatus.BELOW_NORMAL -> AlertWarning
                                LabStatus.ABOVE_NORMAL -> EmergencyRed
                            }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (lab.status) {
                                LabStatus.NORMAL -> SuccessGreenLight
                                LabStatus.BELOW_NORMAL -> AlertWarningBg
                                LabStatus.ABOVE_NORMAL -> EmergencyRedLight
                            }
                        ) {
                            Text(
                                text = when (lab.status) {
                                    LabStatus.NORMAL -> "Normal"
                                    LabStatus.BELOW_NORMAL -> "Below Normal"
                                    LabStatus.ABOVE_NORMAL -> "High"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (lab.status) {
                                    LabStatus.NORMAL -> SuccessGreen
                                    LabStatus.BELOW_NORMAL -> AlertWarning
                                    LabStatus.ABOVE_NORMAL -> EmergencyRed
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Health Summary
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MedicalCyanLight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "AI Health Summary:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NavyDark
                )
                Text(
                    text = report.aiSummary,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onContinueToCare,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Get Care Guidance", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSaveToRecords,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SurfaceSubtle)
        ) {
            Text("Add to Health Records", color = NavyDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
