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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.AppointmentSlot
import com.example.data.HealthFacility
import com.example.data.MedicineStock
import com.example.data.RiskLevel
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
fun CareScreen(
    uiState: ArogyaUiState,
    onFacilitySelect: (HealthFacility) -> Unit,
    onSlotSelect: (AppointmentSlot) -> Unit,
    onBookAppointment: () -> Unit,
    onMedicineQueryChange: (String) -> Unit,
    onShowQrDialog: () -> Unit,
    onLanguageClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedDateIndex by remember { mutableStateOf(0) }

    val categories = listOf("All", "🏥 PHC", "🏨 CHC", "🏩 District Hospital", "💊 Pharmacy")
    val dates = listOf("Today (22 Aug)", "Tomorrow (23 Aug)", "Sat (24 Aug)", "Sun (25 Aug)")

    val filteredMedicines = remember(uiState.medicineSearchQuery, uiState.medicines) {
        if (uiState.medicineSearchQuery.isBlank()) {
            uiState.medicines
        } else {
            uiState.medicines.filter {
                it.medicineName.contains(uiState.medicineSearchQuery, ignoreCase = true) ||
                it.genericName.contains(uiState.medicineSearchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundIceGradientStart.copy(alpha = 0.4f), Color(0xFFF8FAFC))
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

        // Screen Title
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Care Near You",
                    style = MaterialTheme.typography.headlineMedium,
                    color = NavyDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Find the right public healthcare service for your needs.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Recommended Triage Care Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = AlertWarning.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AlertWarning.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = null,
                                tint = AlertWarning,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RECOMMENDED CARE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AlertWarning,
                                letterSpacing = 0.5.sp
                            )
                        }

                        RiskBadge(riskLevel = RiskLevel.MODERATE)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Visit a Primary Health Centre within 24–48 hours.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark
                    )

                    Text(
                        text = "Recommended Facility: PHC Wakad (2.4 km away • Open now with 3 available slots today)",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                uiState.facilities.firstOrNull()?.let { onFacilitySelect(it) }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_select_phc_wakad"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
                        ) {
                            Text("Select PHC Wakad", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .weight(0.9f)
                                .height(44.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalCyanLight)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Directions, contentDescription = null, tint = NavyDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Directions", color = NavyDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) NavyDark else SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) NavyDark else SurfaceBorder
                        ),
                        modifier = Modifier
                            .clickable { selectedCategory = cat }
                            .testTag("care_cat_${cat.take(3)}")
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Active Token & Digital Referral Section (if present)
        if (uiState.activeToken != null) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = NavyDark.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(24.dp),
                    color = NavyDark
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DIGITAL QUEUE TOKEN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MedicalCyan,
                                letterSpacing = 1.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0x33FFFFFF)
                            ) {
                                Text(
                                    text = "CONFIRMED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Your Token",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = uiState.activeToken.tokenNumber,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color(0x33FFFFFF))
                            )

                            Column {
                                Text(
                                    text = "Now Serving",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = uiState.activeToken.currentServingToken,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalCyan
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color(0x33FFFFFF))
                            )

                            Column {
                                Text(
                                    text = "Est. Wait",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "${uiState.activeToken.estimatedWaitMinutes}m",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronIndia
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "${uiState.activeToken.facilityName} • ${uiState.activeToken.appointmentTime}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onShowQrDialog,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("btn_view_referral_qr"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.QrCode, contentDescription = null, tint = NavyDark, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Show Referral QR", color = NavyDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .weight(0.8f)
                                    .height(46.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Navigation, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Directions", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Appointment Booking Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Available Appointments & Queue",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Date selector
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    items(dates.size) { idx ->
                        val isSel = selectedDateIndex == idx
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSel) NavyDark else SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NavyDark else SurfaceBorder),
                            modifier = Modifier.clickable { selectedDateIndex = idx }
                        ) {
                            Text(
                                text = dates[idx],
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color.White else TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                // Facility card selected
                val facility = uiState.selectedFacility ?: uiState.facilities.first()
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = facility.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyDark
                                )
                                Text(
                                    text = "${facility.type} • ${facility.distanceKm} km",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SuccessGreenLight
                            ) {
                                Text(
                                    text = "OPEN NOW",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Select Time Slot:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Slots list
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            facility.availableSlots.forEach { slot ->
                                val isSelected = uiState.selectedSlot == slot
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSelected) MedicalCyanLight else SurfaceSubtle,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) MedicalCyan else SurfaceBorder
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSlotSelect(slot) }
                                        .testTag("slot_${slot.time.replace(" ", "_")}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isSelected) MedicalTeal else Color.Transparent)
                                                    .border(1.5.dp, if (isSelected) MedicalTeal else TextMuted, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isSelected) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(6.dp)
                                                            .clip(CircleShape)
                                                            .background(Color.White)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = slot.time,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = NavyDark
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (slot.isRecommended) {
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = SaffronIndiaLight,
                                                    modifier = Modifier.padding(end = 6.dp)
                                                ) {
                                                    Text(
                                                        text = "RECOMMENDED",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = SaffronIndia,
                                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = if (slot.patientsAhead > 0) "${slot.patientsAhead} patients ahead" else "Available now",
                                                fontSize = 11.sp,
                                                color = if (slot.patientsAhead > 0) TextSecondary else SuccessGreen,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onBookAppointment,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_confirm_book_appointment"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
                        ) {
                            Text("Book Appointment & Generate Token", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Care Journey Timeline Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "My Care Journey",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CareJourneyStage(
                            title = "AI Screening Completed",
                            subtitle = "Skin Irritation assessment (87% confidence)",
                            time = "Aug 22, 10:32 AM",
                            status = JourneyStatus.COMPLETED
                        )
                        CareJourneyStage(
                            title = "Risk Assessment & Triage",
                            subtitle = "Moderate Priority referral to PHC Wakad",
                            time = "Aug 22, 10:33 AM",
                            status = JourneyStatus.COMPLETED
                        )
                        CareJourneyStage(
                            title = "Appointment Confirmed (Token A-42)",
                            subtitle = "PHC Wakad • Today at 12:00 PM",
                            time = "Active Now",
                            status = JourneyStatus.CURRENT
                        )
                        CareJourneyStage(
                            title = "Facility Visit & Clinical Consultation",
                            subtitle = "Dr. Rakesh Patil at OPD Counter 2",
                            time = "Upcoming",
                            status = JourneyStatus.UPCOMING
                        )
                        CareJourneyStage(
                            title = "7-Day Recovery Follow-up",
                            subtitle = "Automatic reminder & recovery check",
                            time = "Aug 28",
                            status = JourneyStatus.UPCOMING
                        )
                    }
                }
            }
        }

        // Medicine Availability Tracker Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Check Medicine Availability",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Real-time stock across government PHCs, CHCs and Jan Aushadhi Kendras",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Search field
                OutlinedTextField(
                    value = uiState.medicineSearchQuery,
                    onValueChange = onMedicineQueryChange,
                    placeholder = { Text("Search medicine (e.g. Paracetamol, IFA, ORS)") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = NavyDark)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_medicine_search"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MedicalTeal,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    filteredMedicines.take(4).forEach { med ->
                        MedicineStockCard(medicine = med)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

enum class JourneyStatus {
    COMPLETED,
    CURRENT,
    UPCOMING
}

@Composable
fun CareJourneyStage(
    title: String,
    subtitle: String,
    time: String,
    status: JourneyStatus
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        when (status) {
                            JourneyStatus.COMPLETED -> SuccessGreen
                            JourneyStatus.CURRENT -> NavyDark
                            JourneyStatus.UPCOMING -> SurfaceSubtle
                        }
                    )
                    .border(
                        1.5.dp,
                        when (status) {
                            JourneyStatus.COMPLETED -> SuccessGreen
                            JourneyStatus.CURRENT -> MedicalCyan
                            JourneyStatus.UPCOMING -> SurfaceBorder
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (status == JourneyStatus.COMPLETED) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                } else if (status == JourneyStatus.CURRENT) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(MedicalCyan))
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (status == JourneyStatus.UPCOMING) TextMuted else NavyDark
                )
                Text(
                    text = time,
                    fontSize = 10.sp,
                    color = if (status == JourneyStatus.CURRENT) SaffronIndia else TextMuted,
                    fontWeight = if (status == JourneyStatus.CURRENT) FontWeight.Bold else FontWeight.Normal
                )
            }
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = if (status == JourneyStatus.UPCOMING) TextMuted else TextSecondary
            )
        }
    }
}

@Composable
fun MedicineStockCard(
    medicine: MedicineStock,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = Color(0x0A000000)),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = medicine.medicineName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = NavyDark
                    )
                    if (medicine.isGovtFreeScheme) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SuccessGreenLight
                        ) {
                            Text(
                                text = "FREE (NHM)",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "${medicine.genericName} • ${medicine.dosage}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    text = "📍 ${medicine.facilityName} (${medicine.distanceKm} km)",
                    fontSize = 10.sp,
                    color = MedicalTeal,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (medicine.isAvailable) SuccessGreenLight else AlertWarningBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (medicine.isAvailable) SuccessGreen else AlertWarning)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (medicine.isAvailable) "In Stock (${medicine.stockUnits})" else "Low Stock",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (medicine.isAvailable) SuccessGreen else AlertWarning
                    )
                }
            }
        }
    }
}
