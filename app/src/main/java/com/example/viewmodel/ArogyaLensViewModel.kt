package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppLanguage
import com.example.data.AppointmentSlot
import com.example.data.AshaPatient
import com.example.data.DigitalReferral
import com.example.data.FollowUpItem
import com.example.data.HealthFacility
import com.example.data.HealthRecordItem
import com.example.data.MedicalReportResult
import com.example.data.MedicineStock
import com.example.data.MockDataProvider
import com.example.data.PatientProfile
import com.example.data.RiskLevel
import com.example.data.ScanMode
import com.example.data.ScanResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppDestination {
    ONBOARDING,
    HOME,
    CARE,
    SCANNER,
    RECORDS,
    PROFILE,
    HEALTH_WORKER
}

enum class ScanProcessState {
    IDLE,
    PREVIEW_CAPTURED,
    ANALYZING,
    ANALYSIS_COMPLETE
}

data class ArogyaUiState(
    val currentDestination: AppDestination = AppDestination.HOME,
    val isOnboarded: Boolean = true,
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val profile: PatientProfile = MockDataProvider.getDefaultProfile(),
    
    // Scanner State
    val scanMode: ScanMode = ScanMode.VISUAL_SYMPTOM,
    val scanProcessState: ScanProcessState = ScanProcessState.IDLE,
    val analysisStep: Int = 0,
    val selectedSampleIndex: Int = 0,
    val currentVisualResult: ScanResult? = null,
    val currentReportResult: MedicalReportResult? = null,
    
    // Care Navigation & Booking State
    val facilities: List<HealthFacility> = MockDataProvider.getFacilities(),
    val selectedFacility: HealthFacility? = MockDataProvider.getFacilities().firstOrNull(),
    val selectedSlot: AppointmentSlot? = MockDataProvider.getFacilities().firstOrNull()?.availableSlots?.getOrNull(1),
    val activeToken: QueueTokenState? = QueueTokenState(
        tokenNumber = "A-42",
        currentServingToken = "A-36",
        estimatedWaitMinutes = 35,
        facilityName = "PHC Wakad",
        appointmentTime = "Today, 12:00 PM",
        referralId = "ARL-2026-48291"
    ),
    val activeReferral: DigitalReferral? = DigitalReferral(
        referralId = "ARL-2026-48291",
        patientName = "Utkarsh T.",
        age = 28,
        gender = "Male",
        facilityName = "PHC Wakad",
        priority = RiskLevel.MODERATE,
        validUntil = "Aug 25, 2026",
        reasonForReferral = "Skin Irritation (Moderate Risk Triage)",
        abhaId = "91-4820-9182-3819"
    ),
    
    // Medicines
    val medicineSearchQuery: String = "",
    val medicines: List<MedicineStock> = MockDataProvider.getMedicineInventory(),
    
    // Records & Followups
    val records: List<HealthRecordItem> = MockDataProvider.getInitialRecords(),
    val recordsFilter: String = "All",
    val followUps: List<FollowUpItem> = MockDataProvider.getFollowUps(),
    
    // ASHA Health Worker
    val ashaPatients: List<AshaPatient> = MockDataProvider.getAshaPatients(),
    val ashaHighRiskCount: Int = 7,
    val ashaFollowupsDue: Int = 12,
    val ashaPendingReferrals: Int = 4,
    val ashaTodayVisits: Int = 18,
    
    // Dialog & Overlay States
    val showEmergencyDialog: Boolean = false,
    val showVoiceAssistOverlay: Boolean = false,
    val showHowItWorksDialog: Boolean = false,
    val showReferralQrDialog: Boolean = false,
    val showLocationPicker: Boolean = false,
    val showAddRecordDialog: Boolean = false,
    val showRegisterPatientDialog: Boolean = false,
    val showSyncSuccessNotification: Boolean = false
)

data class QueueTokenState(
    val tokenNumber: String,
    val currentServingToken: String,
    val estimatedWaitMinutes: Int,
    val facilityName: String,
    val appointmentTime: String,
    val referralId: String
)

class ArogyaLensViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ArogyaUiState())
    val uiState: StateFlow<ArogyaUiState> = _uiState.asStateFlow()

    private val sampleVisualScans = MockDataProvider.getSampleVisualResults()
    private val sampleReportScans = MockDataProvider.getSampleReportResults()

    fun navigateTo(destination: AppDestination) {
        _uiState.update { it.copy(currentDestination = destination) }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun completeOnboarding(name: String, phone: String, location: String) {
        _uiState.update { current ->
            current.copy(
                isOnboarded = true,
                currentDestination = AppDestination.HOME,
                profile = current.profile.copy(
                    name = if (name.isNotBlank()) name else current.profile.name,
                    phone = if (phone.isNotBlank()) phone else current.profile.phone,
                    location = if (location.isNotBlank()) location else current.profile.location
                )
            )
        }
    }

    fun toggleVoiceAssistance(enabled: Boolean) {
        _uiState.update { current ->
            current.copy(
                profile = current.profile.copy(voiceAssistanceEnabled = enabled)
            )
        }
    }

    fun toggleOfflineMode() {
        _uiState.update { current ->
            current.copy(
                profile = current.profile.copy(isOfflineMode = !current.profile.isOfflineMode)
            )
        }
    }

    fun syncOfflineData() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    profile = current.profile.copy(pendingSyncRecords = 0),
                    showSyncSuccessNotification = true
                )
            }
            delay(3000)
            _uiState.update { it.copy(showSyncSuccessNotification = false) }
        }
    }

    // Scanner actions
    fun setScanMode(mode: ScanMode) {
        _uiState.update {
            it.copy(
                scanMode = mode,
                scanProcessState = ScanProcessState.IDLE,
                analysisStep = 0
            )
        }
    }

    fun selectSampleScan(index: Int) {
        _uiState.update { it.copy(selectedSampleIndex = index) }
    }

    fun captureScan() {
        _uiState.update { it.copy(scanProcessState = ScanProcessState.PREVIEW_CAPTURED) }
    }

    fun retakeScan() {
        _uiState.update {
            it.copy(
                scanProcessState = ScanProcessState.IDLE,
                analysisStep = 0
            )
        }
    }

    fun runAiAnalysis() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    scanProcessState = ScanProcessState.ANALYZING,
                    analysisStep = 1
                )
            }
            delay(900)
            _uiState.update { it.copy(analysisStep = 2) }
            delay(900)
            _uiState.update { it.copy(analysisStep = 3) }
            delay(800)

            val currentState = _uiState.value
            if (currentState.scanMode == ScanMode.VISUAL_SYMPTOM) {
                val result = sampleVisualScans[currentState.selectedSampleIndex % sampleVisualScans.size]
                _uiState.update {
                    it.copy(
                        scanProcessState = ScanProcessState.ANALYSIS_COMPLETE,
                        currentVisualResult = result
                    )
                }
            } else {
                val reportResult = sampleReportScans.first()
                _uiState.update {
                    it.copy(
                        scanProcessState = ScanProcessState.ANALYSIS_COMPLETE,
                        currentReportResult = reportResult
                    )
                }
            }
        }
    }

    fun addCurrentScanToRecords() {
        val visual = _uiState.value.currentVisualResult
        val report = _uiState.value.currentReportResult
        val newRecord = if (visual != null && _uiState.value.scanMode == ScanMode.VISUAL_SYMPTOM) {
            HealthRecordItem(
                id = "rec-${System.currentTimeMillis()}",
                title = "Visual Screening - ${visual.conditionName}",
                category = "Scan",
                date = "TODAY",
                summary = "${visual.conditionName} (${visual.confidenceScore}% confidence). ${visual.riskLevel} Priority.",
                riskLevel = visual.riskLevel,
                facilityOrDoctor = "ArogyaLens AI Triager",
                detailNotes = visual.description
            )
        } else if (report != null) {
            HealthRecordItem(
                id = "rec-${System.currentTimeMillis()}",
                title = "Medical Report - ${report.testTitle}",
                category = "Report",
                date = "TODAY",
                summary = report.aiSummary,
                riskLevel = report.riskLevel,
                facilityOrDoctor = "ArogyaLens Document AI",
                detailNotes = report.recommendedAction
            )
        } else null

        if (newRecord != null) {
            _uiState.update { current ->
                current.copy(records = listOf(newRecord) + current.records)
            }
        }
    }

    // Care & Booking actions
    fun selectFacility(facility: HealthFacility) {
        _uiState.update {
            it.copy(
                selectedFacility = facility,
                selectedSlot = facility.availableSlots.firstOrNull()
            )
        }
    }

    fun selectSlot(slot: AppointmentSlot) {
        _uiState.update { it.copy(selectedSlot = slot) }
    }

    fun bookAppointment() {
        val facility = _uiState.value.selectedFacility ?: return
        val slot = _uiState.value.selectedSlot ?: return
        
        val randomTokenNum = "A-${(38..48).random()}"
        val newToken = QueueTokenState(
            tokenNumber = randomTokenNum,
            currentServingToken = "A-36",
            estimatedWaitMinutes = (slot.patientsAhead * 10).coerceAtLeast(15),
            facilityName = facility.name,
            appointmentTime = "Today, ${slot.time}",
            referralId = "ARL-2026-${(10000..99999).random()}"
        )
        val newReferral = DigitalReferral(
            referralId = newToken.referralId,
            patientName = _uiState.value.profile.name,
            age = 28,
            gender = "Male",
            facilityName = facility.name,
            priority = RiskLevel.MODERATE,
            validUntil = "Aug 25, 2026",
            reasonForReferral = "Triage Consultation for ${facility.name}",
            abhaId = _uiState.value.profile.abhaId
        )
        val newRecord = HealthRecordItem(
            id = "rec-ref-${System.currentTimeMillis()}",
            title = "Digital Health Referral (${newToken.referralId})",
            category = "Referral",
            date = "TODAY",
            summary = "Token ${newToken.tokenNumber} issued for ${facility.name} at ${slot.time}.",
            riskLevel = RiskLevel.MODERATE,
            facilityOrDoctor = facility.name,
            detailNotes = "Appointment confirmed with ${slot.patientsAhead} patients ahead."
        )

        _uiState.update { current ->
            current.copy(
                activeToken = newToken,
                activeReferral = newReferral,
                records = listOf(newRecord) + current.records
            )
        }
    }

    fun updateMedicineSearch(query: String) {
        _uiState.update { it.copy(medicineSearchQuery = query) }
    }

    fun setRecordsFilter(filter: String) {
        _uiState.update { it.copy(recordsFilter = filter) }
    }

    fun toggleFollowUpComplete(id: String) {
        _uiState.update { current ->
            current.copy(
                followUps = current.followUps.map {
                    if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
                }
            )
        }
    }

    fun addNewCustomRecord(title: String, category: String, summary: String) {
        val item = HealthRecordItem(
            id = "rec-${System.currentTimeMillis()}",
            title = title.ifBlank { "Health Record" },
            category = category,
            date = "TODAY",
            summary = summary.ifBlank { "Added by user" },
            riskLevel = RiskLevel.LOW,
            facilityOrDoctor = "Self Entry / PHC",
            detailNotes = "User generated record entry"
        )
        _uiState.update { current ->
            current.copy(
                records = listOf(item) + current.records,
                showAddRecordDialog = false
            )
        }
    }

    fun registerAshaPatient(name: String, age: Int, village: String, condition: String, risk: RiskLevel) {
        val newPatient = AshaPatient(
            id = "asha-${System.currentTimeMillis()}",
            name = name,
            age = age,
            villageArea = village,
            conditionSummary = condition,
            riskLevel = risk,
            referralStatus = "Registered & Scheduled",
            lastVisited = "Today",
            phoneNumber = "+91 98000 ${ (10000..99999).random() }"
        )
        _uiState.update { current ->
            current.copy(
                ashaPatients = listOf(newPatient) + current.ashaPatients,
                ashaTodayVisits = current.ashaTodayVisits + 1,
                showRegisterPatientDialog = false
            )
        }
    }

    // Dialog toggles
    fun setEmergencyDialogVisible(show: Boolean) {
        _uiState.update { it.copy(showEmergencyDialog = show) }
    }

    fun setVoiceAssistOverlayVisible(show: Boolean) {
        _uiState.update { it.copy(showVoiceAssistOverlay = show) }
    }

    fun setHowItWorksDialogVisible(show: Boolean) {
        _uiState.update { it.copy(showHowItWorksDialog = show) }
    }

    fun setReferralQrDialogVisible(show: Boolean) {
        _uiState.update { it.copy(showReferralQrDialog = show) }
    }

    fun setLocationPickerVisible(show: Boolean) {
        _uiState.update { it.copy(showLocationPicker = show) }
    }

    fun setAddRecordDialogVisible(show: Boolean) {
        _uiState.update { it.copy(showAddRecordDialog = show) }
    }

    fun setRegisterPatientDialogVisible(show: Boolean) {
        _uiState.update { it.copy(showRegisterPatientDialog = show) }
    }

    fun updateLocation(newLocation: String) {
        _uiState.update { current ->
            current.copy(
                profile = current.profile.copy(location = newLocation),
                showLocationPicker = false
            )
        }
    }
}
