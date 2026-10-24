package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.HealthFacility
import com.example.data.Localization
import com.example.data.RiskLevel
import com.example.data.ScanMode
import com.example.ui.components.ArogyaTopHeader
import com.example.ui.components.OfflineStatusBanner
import com.example.ui.components.RiskBadge
import com.example.ui.theme.AlertWarning
import com.example.ui.theme.AlertWarningBg
import com.example.ui.theme.BackgroundIceGradientStart
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedLight
import com.example.ui.theme.MedicalCyan
import com.example.ui.theme.MedicalCyanLight
import com.example.ui.theme.MedicalTeal
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyMedium
import com.example.ui.theme.SaffronIndia
import com.example.ui.theme.SaffronIndiaLight
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

@Composable
fun HomeScreen(
    uiState: ArogyaUiState,
    onNavigate: (AppDestination) -> Unit,
    onOpenScannerMode: (ScanMode) -> Unit,
    onLanguageClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onLocationClick: () -> Unit,
    onEmergencyClick: () -> Unit,
    onSyncClick: () -> Unit,
    onSelectFacility: (HealthFacility) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundIceGradientStart.copy(alpha = 0.5f), Color(0xFFF8FAFC))
                )
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            ArogyaTopHeader(
                locationText = uiState.profile.location.split(",").firstOrNull() ?: "Wakad, Pune",
                currentLanguage = uiState.currentLanguage,
                onLanguageClick = onLanguageClick,
                onVoiceClick = onVoiceClick,
                onLocationClick = onLocationClick,
                pendingSyncCount = uiState.profile.pendingSyncRecords
            )
        }

        // Greeting & Welcome
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = Localization.getGreeting(uiState.currentLanguage, uiState.profile.name.split(" ").firstOrNull() ?: "Utkarsh"),
                    style = MaterialTheme.typography.headlineMedium,
                    color = NavyDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = Localization.getSubtitle(uiState.currentLanguage),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Offline Status Banner
        item {
            OfflineStatusBanner(
                isOffline = uiState.profile.isOfflineMode,
                pendingSyncCount = uiState.profile.pendingSyncRecords,
                onSyncClick = onSyncClick
            )
        }

        // Hero AI Screening Card - Primary Call to Action
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(12.dp, RoundedCornerShape(26.dp), spotColor = NavyDark.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(26.dp),
                color = NavyDark
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MedicalCyan.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = MedicalCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "ArogyaLens AI Triager",
                                color = MedicalCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x22FFFFFF)
                        ) {
                            Text(
                                text = "Offline AI Ready",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Check a Health Concern",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Scan a visible symptom or medical report to receive instant risk triage & nearest PHC guidance.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onOpenScannerMode(ScanMode.VISUAL_SYMPTOM) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("home_scan_symptom_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = NavyDark,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Scan Symptom",
                                    color = NavyDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Button(
                            onClick = { onOpenScannerMode(ScanMode.MEDICAL_REPORT) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("home_scan_report_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Scan Report",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Queue Token Card (if booked)
        if (uiState.activeToken != null) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clickable { onNavigate(AppDestination.CARE) }
                        .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x10000000)),
                    shape = RoundedCornerShape(22.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MedicalTeal.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MedicalCyanLight
                                ) {
                                    Text(
                                        text = "LIVE QUEUE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MedicalTeal,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiState.activeToken.facilityName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NavyDark
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your Token: ${uiState.activeToken.tokenNumber}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NavyDark
                            )
                            Text(
                                text = "Current: ${uiState.activeToken.currentServingToken} • Est. Wait: ${uiState.activeToken.estimatedWaitMinutes} mins",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MedicalCyanLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "View Queue",
                                tint = NavyDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // My Health Status Metrics
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "My Health Status",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Overall Health
                    HealthMetricCard(
                        title = "Overall Health",
                        value = "Stable",
                        subtitle = "Vitals Normal",
                        accentColor = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )

                    // Latest Assessment
                    HealthMetricCard(
                        title = "Latest Triage",
                        value = "Moderate",
                        subtitle = "Skin Irritation",
                        accentColor = AlertWarning,
                        modifier = Modifier.weight(1f)
                    )

                    // Next Follow-up
                    HealthMetricCard(
                        title = "Follow-up",
                        value = "Aug 28",
                        subtitle = "PHC Wakad",
                        accentColor = MedicalTeal,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Doctor Spotlight / On-Duty Tele-Triage (Matching attached screenshot aesthetic!)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Doctor on Duty • PHC Wakad",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(26.dp), spotColor = Color(0x1509223B)),
                    shape = RoundedCornerShape(26.dp),
                    color = SurfaceWhite
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Brush.linearGradient(listOf(MedicalCyanLight, BackgroundIceGradientStart))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalHospital,
                                    contentDescription = "Doctor",
                                    tint = NavyDark,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SaffronIndiaLight
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = SaffronIndia,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "4.8",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SaffronIndia
                                            )
                                        }
                                    }
                                    Text(
                                        text = "• Available Now",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SuccessGreen
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Dr. Rakesh Patil",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyDark
                                )
                                Text(
                                    text = "MBBS, DNB • Medical Officer",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Row (like the reference screenshot: 8 Years, 3.5k+ Patients, 2.8k+ Reviews)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(SurfaceSubtle)
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "8+ Years",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NavyDark
                                )
                                Text(
                                    text = "Experience",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(SurfaceBorder)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "3.5k+",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NavyDark
                                )
                                Text(
                                    text = "Patients",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(SurfaceBorder)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Free OPD",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SuccessGreen
                                )
                                Text(
                                    text = "Govt NHM",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action pills (Details, Book, Referral)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { onNavigate(AppDestination.CARE) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
                            ) {
                                Text("Book OPD Slot", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(SurfaceSubtle)
                                    .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                                    .clickable { onNavigate(AppDestination.CARE) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Call Facility",
                                    tint = NavyDark,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Find Healthcare",
                        subtitle = "PHCs & Hospitals",
                        icon = Icons.Default.LocalHospital,
                        iconTint = MedicalTeal,
                        onClick = { onNavigate(AppDestination.CARE) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_find_care"
                    )

                    QuickActionCard(
                        title = "Medicine Stock",
                        subtitle = "Nearby Availability",
                        icon = Icons.Default.LocalPharmacy,
                        iconTint = MedicalTeal,
                        onClick = { onNavigate(AppDestination.CARE) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_medicine_stock"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "My Appointments",
                        subtitle = "Tokens & Schedule",
                        icon = Icons.Default.CalendarMonth,
                        iconTint = NavyMedium,
                        onClick = { onNavigate(AppDestination.CARE) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_appointments"
                    )

                    QuickActionCard(
                        title = "Health Records",
                        subtitle = "Reports & Scans",
                        icon = Icons.Default.Description,
                        iconTint = SaffronIndia,
                        onClick = { onNavigate(AppDestination.RECORDS) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_health_records"
                    )
                }
            }
        }

        // Emergency Help Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(onClick = onEmergencyClick)
                    .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = EmergencyRed.copy(alpha = 0.2f))
                    .testTag("home_emergency_banner"),
                shape = RoundedCornerShape(20.dp),
                color = EmergencyRedLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(EmergencyRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = "Emergency",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "🚨 Emergency Help (108)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = EmergencyRed
                            )
                            Text(
                                text = "Direct ambulance dispatch & 24x7 trauma care",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Emergency Assistance",
                        tint = EmergencyRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Nearby Care Facilities
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nearby Care",
                        style = MaterialTheme.typography.titleMedium,
                        color = NavyDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.labelMedium,
                        color = MedicalTeal,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigate(AppDestination.CARE) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                uiState.facilities.take(2).forEach { facility ->
                    NearbyFacilityCard(
                        facility = facility,
                        onSelect = {
                            onSelectFacility(facility)
                            onNavigate(AppDestination.CARE)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        // Bottom spacing so content isn't covered by bottom nav bar
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HealthMetricCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x0D000000)),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NavyDark
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x0D000000))
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowOutward,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = NavyDark
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun NearbyFacilityCard(
    facility: HealthFacility,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onSelect)
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x0D000000)),
        shape = RoundedCornerShape(20.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = facility.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                    )
                }
                Text(
                    text = "${facility.type} • ${facility.distanceKm} km away",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Text(
                    text = "Doctor: ${facility.doctorOnDuty}",
                    fontSize = 11.sp,
                    color = MedicalTeal,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NavyDark
            ) {
                Text(
                    text = "View",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}
