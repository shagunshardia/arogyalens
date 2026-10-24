package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.HealthRecordItem
import com.example.ui.components.ArogyaTopHeader
import com.example.ui.components.RiskBadge
import com.example.ui.theme.AlertWarning
import com.example.ui.theme.AlertWarningBg
import com.example.ui.theme.BackgroundIceGradientStart
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
import com.example.viewmodel.ArogyaUiState

@Composable
fun RecordsScreen(
    uiState: ArogyaUiState,
    onFilterChange: (String) -> Unit,
    onToggleFollowUp: (String) -> Unit,
    onAddRecordClick: () -> Unit,
    onShowQrClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf("All", "Scans", "Reports", "Visits", "Referrals")
    var selectedRecordDetail by remember { mutableStateOf<HealthRecordItem?>(null) }

    val filteredRecords = remember(uiState.recordsFilter, uiState.records) {
        if (uiState.recordsFilter == "All") {
            uiState.records
        } else {
            uiState.records.filter { it.category.equals(uiState.recordsFilter, ignoreCase = true) }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BackgroundIceGradientStart.copy(alpha = 0.4f), Color(0xFFF8FAFC))
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
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

            // Title
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "My Health Records",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NavyDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Longitudinal medical history & Ayushman Bharat linked records",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            // Patient Health Profile Summary Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x1009223B)),
                    shape = RoundedCornerShape(22.dp),
                    color = NavyDark
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "HEALTH ID / ABHA",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalCyan,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = uiState.profile.abhaId,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0x33FFFFFF),
                                modifier = Modifier.clickable { onShowQrClick() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = "QR",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("QR Card", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ProfileInfoItem(label = "Blood Group", value = uiState.profile.bloodGroup)
                            ProfileInfoItem(label = "Allergies", value = uiState.profile.allergies)
                            ProfileInfoItem(label = "Chronic Cond.", value = uiState.profile.chronicConditions)
                        }
                    }
                }
            }

            // Follow-up Alerts & Tasks
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Follow-ups & Care Reminders",
                        style = MaterialTheme.typography.titleMedium,
                        color = NavyDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.followUps.forEach { fup ->
                            FollowUpCard(
                                item = fup,
                                onToggle = { onToggleFollowUp(fup.id) }
                            )
                        }
                    }
                }
            }

            // Filter Tabs
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        val isSelected = uiState.recordsFilter.equals(filter, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) NavyDark else SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) NavyDark else SurfaceBorder
                            ),
                            modifier = Modifier
                                .clickable { onFilterChange(filter) }
                                .testTag("record_tab_$filter")
                        ) {
                            Text(
                                text = filter,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextPrimary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // Medical History Records Timeline
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Medical Timeline (${filteredRecords.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = NavyDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        filteredRecords.forEach { record ->
                            RecordTimelineCard(
                                record = record,
                                onView = { selectedRecordDetail = record }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Floating Action Button to Add Record
        FloatingActionButton(
            onClick = onAddRecordClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 90.dp)
                .testTag("fab_add_record"),
            containerColor = NavyDark,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Record")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Record", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        // Record Detail Dialog
        if (selectedRecordDetail != null) {
            val record = selectedRecordDetail!!
            androidx.compose.ui.window.Dialog(
                onDismissRequest = { selectedRecordDetail = null }
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceWhite,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = record.category.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MedicalTeal,
                                letterSpacing = 1.sp
                            )
                            if (record.riskLevel != null) {
                                RiskBadge(riskLevel = record.riskLevel)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = record.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )

                        Text(
                            text = "Date: ${record.date} • Facility: ${record.facilityOrDoctor}",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceSubtle,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Clinical Summary:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = NavyDark
                                )
                                Text(
                                    text = record.summary,
                                    fontSize = 13.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { selectedRecordDetail = null },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
                        ) {
                            Text("Close", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun FollowUpCard(
    item: com.example.data.FollowUpItem,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = Color(0x0A000000)),
        shape = RoundedCornerShape(16.dp),
        color = if (item.isCompleted) SuccessGreenLight else SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (item.isCompleted) SuccessGreen.copy(alpha = 0.4f) else SurfaceBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (item.isCompleted) SuccessGreen else TextMuted,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (item.isCompleted) SuccessGreen else NavyDark
                    )
                    Text(
                        text = item.subtitle,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (item.isCompleted) SuccessGreenLight else SaffronIndiaLight
            ) {
                Text(
                    text = item.dueDaysText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.isCompleted) SuccessGreen else SaffronIndia,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
fun RecordTimelineCard(
    record: HealthRecordItem,
    onView: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(18.dp), spotColor = Color(0x0A000000)),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when (record.category) {
                                    "Scan" -> MedicalCyanLight
                                    "Report" -> SaffronIndiaLight
                                    "Visit" -> SuccessGreenLight
                                    else -> SurfaceSubtle
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (record.category) {
                                "Scan" -> Icons.Default.QrCodeScanner
                                "Report" -> Icons.Default.Description
                                "Visit" -> Icons.Default.LocalHospital
                                else -> Icons.Default.Assignment
                            },
                            contentDescription = null,
                            tint = NavyDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = record.date,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                if (record.riskLevel != null) {
                    RiskBadge(riskLevel = record.riskLevel)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = record.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = NavyDark
            )

            Text(
                text = record.summary,
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Doctor/Source: ${record.facilityOrDoctor}",
                    fontSize = 11.sp,
                    color = MedicalTeal,
                    fontWeight = FontWeight.Medium
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceSubtle,
                    modifier = Modifier.clickable(onClick = onView)
                ) {
                    Text(
                        text = "View Details",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
