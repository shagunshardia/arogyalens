package com.example.data

enum class AppLanguage(val displayName: String, val code: String) {
    ENGLISH("English", "en"),
    HINDI("हिंदी", "hi"),
    MARATHI("मराठी", "mr")
}

enum class RiskLevel {
    LOW,
    MODERATE,
    HIGH
}

enum class ScanMode {
    VISUAL_SYMPTOM,
    MEDICAL_REPORT
}

data class ExtractedLabValue(
    val parameter: String,
    val resultValue: String,
    val normalRange: String,
    val status: LabStatus
)

enum class LabStatus {
    NORMAL,
    BELOW_NORMAL,
    ABOVE_NORMAL
}

data class ScanResult(
    val id: String,
    val conditionName: String,
    val confidenceScore: Int,
    val riskLevel: RiskLevel,
    val description: String,
    val keyObservations: List<String>,
    val careGuidance: String,
    val recommendedFacilityType: String,
    val timestamp: String
)

data class MedicalReportResult(
    val id: String,
    val testTitle: String,
    val patientName: String,
    val testDate: String,
    val labValues: List<ExtractedLabValue>,
    val aiSummary: String,
    val riskLevel: RiskLevel,
    val recommendedAction: String
)

data class AppointmentSlot(
    val time: String,
    val patientsAhead: Int,
    val isRecommended: Boolean = false,
    val isAvailable: Boolean = true
)

data class HealthFacility(
    val id: String,
    val name: String,
    val type: String, // PHC, CHC, District Hospital, Specialist, Pharmacy
    val distanceKm: Double,
    val isOpen: Boolean,
    val timings: String,
    val address: String,
    val doctorOnDuty: String,
    val currentWaitTimeMinutes: Int,
    val emergency24x7: Boolean = false,
    val availableSlots: List<AppointmentSlot> = emptyList()
)

data class QueueToken(
    val tokenNumber: String,
    val currentServingToken: String,
    val estimatedWaitMinutes: Int,
    val facilityName: String,
    val appointmentTime: String,
    val referralId: String,
    val bookedDate: String,
    val patientName: String
)

data class DigitalReferral(
    val referralId: String,
    val patientName: String,
    val age: Int,
    val gender: String,
    val facilityName: String,
    val priority: RiskLevel,
    val validUntil: String,
    val reasonForReferral: String,
    val abhaId: String
)

data class FollowUpItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val dueDaysText: String,
    val isCompleted: Boolean,
    val category: String
)

data class MedicineStock(
    val id: String,
    val medicineName: String,
    val dosage: String,
    val genericName: String,
    val facilityName: String,
    val facilityType: String,
    val distanceKm: Double,
    val isAvailable: Boolean,
    val stockUnits: Int,
    val isGovtFreeScheme: Boolean = true
)

data class HealthRecordItem(
    val id: String,
    val title: String,
    val category: String, // Scan, Report, Visit, Referral
    val date: String,
    val summary: String,
    val riskLevel: RiskLevel?,
    val facilityOrDoctor: String,
    val detailNotes: String
)

data class PatientProfile(
    val name: String,
    val phone: String,
    val location: String,
    val bloodGroup: String,
    val abhaId: String,
    val allergies: String,
    val chronicConditions: String,
    val emergencyContactName: String,
    val emergencyContactPhone: String,
    val voiceAssistanceEnabled: Boolean = false,
    val isOfflineMode: Boolean = true,
    val pendingSyncRecords: Int = 3
)

data class AshaPatient(
    val id: String,
    val name: String,
    val age: Int,
    val villageArea: String,
    val conditionSummary: String,
    val riskLevel: RiskLevel,
    val referralStatus: String,
    val lastVisited: String,
    val phoneNumber: String
)
