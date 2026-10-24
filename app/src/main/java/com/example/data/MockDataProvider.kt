package com.example.data

object MockDataProvider {

    fun getDefaultProfile(): PatientProfile = PatientProfile(
        name = "Utkarsh T.",
        phone = "+91 98765 43210",
        location = "Wakad, Pune, Maharashtra",
        bloodGroup = "O+",
        abhaId = "91-4820-9182-3819",
        allergies = "None Added",
        chronicConditions = "None Added",
        emergencyContactName = "Dr. Anjali Deshmukh / Family",
        emergencyContactPhone = "+91 98220 11223",
        voiceAssistanceEnabled = false,
        isOfflineMode = true,
        pendingSyncRecords = 3
    )

    fun getFacilities(): List<HealthFacility> = listOf(
        HealthFacility(
            id = "phc-wakad",
            name = "PHC Wakad",
            type = "Primary Health Centre",
            distanceKm = 2.4,
            isOpen = true,
            timings = "8:00 AM - 6:00 PM",
            address = "Survey 42, Near Datta Mandir, Wakad, Pune",
            doctorOnDuty = "Dr. Rakesh Patil (MBBS)",
            currentWaitTimeMinutes = 35,
            emergency24x7 = false,
            availableSlots = listOf(
                AppointmentSlot("10:30 AM", patientsAhead = 8, isRecommended = false, isAvailable = true),
                AppointmentSlot("12:00 PM", patientsAhead = 3, isRecommended = true, isAvailable = true),
                AppointmentSlot("02:30 PM", patientsAhead = 1, isRecommended = false, isAvailable = true),
                AppointmentSlot("04:30 PM", patientsAhead = 0, isRecommended = false, isAvailable = true)
            )
        ),
        HealthFacility(
            id = "chc-baner",
            name = "Community Health Centre Baner",
            type = "Community Health Centre (CHC)",
            distanceKm = 6.8,
            isOpen = true,
            timings = "24 Hours (OPD 8 AM - 4 PM)",
            address = "Baner-Pashan Link Road, Pune",
            doctorOnDuty = "Dr. Priya Kulkarni (MD Medicine)",
            currentWaitTimeMinutes = 50,
            emergency24x7 = true,
            availableSlots = listOf(
                AppointmentSlot("11:00 AM", patientsAhead = 12, isRecommended = false, isAvailable = true),
                AppointmentSlot("01:30 PM", patientsAhead = 5, isRecommended = true, isAvailable = true),
                AppointmentSlot("03:30 PM", patientsAhead = 2, isRecommended = false, isAvailable = true)
            )
        ),
        HealthFacility(
            id = "dh-aundh",
            name = "Pune District Hospital",
            type = "District Hospital",
            distanceKm = 9.2,
            isOpen = true,
            timings = "24x7 Emergency & Trauma",
            address = "Aundh Chest Hospital Campus, Pune",
            doctorOnDuty = "Dr. S. K. Joshi (Chief Medical Officer)",
            currentWaitTimeMinutes = 65,
            emergency24x7 = true,
            availableSlots = listOf(
                AppointmentSlot("10:00 AM", patientsAhead = 20, isRecommended = false, isAvailable = true),
                AppointmentSlot("02:00 PM", patientsAhead = 8, isRecommended = true, isAvailable = true)
            )
        ),
        HealthFacility(
            id = "jan-aushadhi-wakad",
            name = "Pradhan Mantri Jan Aushadhi Kendra",
            type = "Generic Pharmacy",
            distanceKm = 1.8,
            isOpen = true,
            timings = "9:00 AM - 9:00 PM",
            address = "Shop 4, Kaspate Vasti, Wakad",
            doctorOnDuty = "Pharmacist Sunil Shinde",
            currentWaitTimeMinutes = 10,
            emergency24x7 = false
        )
    )

    fun getSampleVisualResults(): List<ScanResult> = listOf(
        ScanResult(
            id = "scan-skin-01",
            conditionName = "Skin Irritation / Contact Dermatitis",
            confidenceScore = 87,
            riskLevel = RiskLevel.MODERATE,
            description = "Erythematous localized patch with mild surface scaling detected on epidermal layer. No signs of deep tissue infection.",
            keyObservations = listOf(
                "Localized redness around epidermal barrier",
                "Mild follicular elevation without purulent discharge",
                "No systemic necrosis or acute cellulitis spread detected"
            ),
            careGuidance = "Visit PHC Wakad within 24–48 hours for topical prescription. Avoid harsh soaps or scratching. Keep the area dry and clean.",
            recommendedFacilityType = "Primary Health Centre",
            timestamp = "Aug 22, 2026 • 10:32 AM"
        ),
        ScanResult(
            id = "scan-eye-02",
            conditionName = "Mild Allergic Conjunctivitis",
            confidenceScore = 92,
            riskLevel = RiskLevel.LOW,
            description = "Scleral hyperemia with minimal watery discharge. Cornea remains clear with intact pupillary response.",
            keyObservations = listOf(
                "Bilateral conjunctival injection without photophobia",
                "Clear corneal reflex",
                "No signs of corneal ulceration or acute glaucoma"
            ),
            careGuidance = "Apply cool compress. Use OTC lubricating eye drops. If redness persists over 3 days or vision blurs, consult PHC doctor.",
            recommendedFacilityType = "Primary Health Centre",
            timestamp = "Aug 15, 2026 • 04:15 PM"
        ),
        ScanResult(
            id = "scan-wound-03",
            conditionName = "Deep Diabetic Foot Ulceration",
            confidenceScore = 94,
            riskLevel = RiskLevel.HIGH,
            description = "Subcutaneous ulceration with surrounding periwound erythema and tissue slough. High risk of secondary bacterial infection.",
            keyObservations = listOf(
                "Ulcer depth penetrating dermis",
                "Marked peripheral edema and warmth",
                "Urgent wound debridement and glycemic stabilization required"
            ),
            careGuidance = "Immediate attention required. Visit District Hospital or CHC Trauma care today for wound care and intravenous evaluation.",
            recommendedFacilityType = "District Hospital / CHC Emergency",
            timestamp = "Aug 21, 2026 • 09:10 AM"
        )
    )

    fun getSampleReportResults(): List<MedicalReportResult> = listOf(
        MedicalReportResult(
            id = "rep-cbc-01",
            testTitle = "Complete Blood Count (CBC)",
            patientName = "Utkarsh T.",
            testDate = "Aug 18, 2026",
            labValues = listOf(
                ExtractedLabValue("Hemoglobin (Hb)", "10.2 g/dL", "13.0 - 17.0 g/dL", LabStatus.BELOW_NORMAL),
                ExtractedLabValue("Fasting Blood Sugar", "102 mg/dL", "70 - 100 mg/dL", LabStatus.NORMAL),
                ExtractedLabValue("Total WBC Count", "7,400 /mcL", "4,000 - 11,000 /mcL", LabStatus.NORMAL),
                ExtractedLabValue("Platelet Count", "2.4 Lakh/mcL", "1.5 - 4.5 Lakh/mcL", LabStatus.NORMAL)
            ),
            aiSummary = "Your hemoglobin level (10.2 g/dL) is below normal, indicating mild nutritional anemia. Blood glucose and cell counts are within standard range.",
            riskLevel = RiskLevel.MODERATE,
            recommendedAction = "Consult PHC Wakad physician for Iron & Folic Acid supplementation and dietary counseling (green leafy vegetables, jaggery)."
        )
    )

    fun getMedicineInventory(): List<MedicineStock> = listOf(
        MedicineStock("med-1", "Paracetamol 500mg", "Tablet", "Paracetamol IP", "PHC Wakad", "PHC", 2.4, true, 340, true),
        MedicineStock("med-2", "Iron & Folic Acid (IFA)", "Tablet (100mg/0.5mg)", "Ferrous Sulphate + Folic Acid", "PHC Wakad", "PHC", 2.4, true, 850, true),
        MedicineStock("med-3", "Oral Rehydration Salts (ORS)", "Sachet 21.8g", "WHO Formula ORS", "PHC Wakad", "PHC", 2.4, true, 190, true),
        MedicineStock("med-4", "Amoxicillin 500mg", "Capsule", "Amoxicillin Trihydrate", "CHC Baner", "CHC", 6.8, true, 210, true),
        MedicineStock("med-5", "Metformin 500mg", "Tablet", "Metformin Hydrochloride", "Pune District Hospital", "Hospital", 9.2, true, 1200, true),
        MedicineStock("med-6", "Cetirizine 10mg", "Tablet", "Cetirizine Hydrochloride", "Jan Aushadhi Kendra", "Pharmacy", 1.8, true, 450, false),
        MedicineStock("med-7", "Ciprofloxacin Eye Drops", "Drops 5ml", "Ciprofloxacin 0.3%", "PHC Wakad", "PHC", 2.4, true, 75, true)
    )

    fun getFollowUps(): List<FollowUpItem> = listOf(
        FollowUpItem(
            id = "fup-1",
            title = "PHC Wakad Consultation",
            subtitle = "Skin Assessment follow-up and topical prescription check",
            dueDaysText = "Today, 12:00 PM",
            isCompleted = false,
            category = "Doctor Visit"
        ),
        FollowUpItem(
            id = "fup-2",
            title = "Nutritional Anemia Follow-up",
            subtitle = "Re-check Hb levels after 30 days of Iron & Folic Acid tablets",
            dueDaysText = "Due in 14 days",
            isCompleted = false,
            category = "Lab Test"
        ),
        FollowUpItem(
            id = "fup-3",
            title = "Eye Health Screening",
            subtitle = "Follow-up check for seasonal conjunctivitis clearance",
            dueDaysText = "Completed",
            isCompleted = true,
            category = "Self Check"
        )
    )

    fun getInitialRecords(): List<HealthRecordItem> = listOf(
        HealthRecordItem(
            id = "rec-1",
            title = "Visual Screening - Skin Irritation",
            category = "Scan",
            date = "AUG 22, 2026",
            summary = "Erythematous patch identified. Risk Level: MODERATE. Referral generated.",
            riskLevel = RiskLevel.MODERATE,
            facilityOrDoctor = "ArogyaLens AI Triager",
            detailNotes = "AI screening detected surface erythema on forearm. Recommended PHC consultation."
        ),
        HealthRecordItem(
            id = "rec-2",
            title = "Blood Test Report (CBC)",
            category = "Report",
            date = "AUG 18, 2026",
            summary = "Hemoglobin 10.2 g/dL (Mild Anemia). Fasting sugar 102 mg/dL.",
            riskLevel = RiskLevel.MODERATE,
            facilityOrDoctor = "Wakad Diagnostic Lab",
            detailNotes = "Patient prescribed oral iron supplements under NHM guidelines."
        ),
        HealthRecordItem(
            id = "rec-3",
            title = "PHC Wakad General OPD Visit",
            category = "Visit",
            date = "AUG 10, 2026",
            summary = "Routine wellness checkup. BP: 120/80 mmHg, Pulse: 72 bpm.",
            riskLevel = RiskLevel.LOW,
            facilityOrDoctor = "Dr. Rakesh Patil (PHC Wakad)",
            detailNotes = "Vitals stable. Advised regular physical exercise and hydration."
        ),
        HealthRecordItem(
            id = "rec-4",
            title = "Digital Health Referral (ARL-2026-48291)",
            category = "Referral",
            date = "AUG 22, 2026",
            summary = "Digital Token A-42 assigned at PHC Wakad. Valid until Aug 25.",
            riskLevel = RiskLevel.MODERATE,
            facilityOrDoctor = "PHC Wakad",
            detailNotes = "Priority triage referral generated for skin irritation checkup."
        )
    )

    fun getAshaPatients(): List<AshaPatient> = listOf(
        AshaPatient(
            id = "asha-p1",
            name = "Meena Devi",
            age = 42,
            villageArea = "Wakad Sector 4 (Basti)",
            conditionSummary = "Acute eye redness with photophobia, referral pending",
            riskLevel = RiskLevel.HIGH,
            referralStatus = "Referral Pending (CHC Baner)",
            lastVisited = "Yesterday",
            phoneNumber = "+91 94220 88712"
        ),
        AshaPatient(
            id = "asha-p2",
            name = "Ramesh Kumar",
            age = 58,
            villageArea = "Kaspate Wasti",
            conditionSummary = "Missed monthly Hypertension follow-up & BP refill",
            riskLevel = RiskLevel.MODERATE,
            referralStatus = "Follow-up Overdue (3 days)",
            lastVisited = "Aug 12, 2026",
            phoneNumber = "+91 98901 33441"
        ),
        AshaPatient(
            id = "asha-p3",
            name = "Sunita Patil",
            age = 26,
            villageArea = "Datta Mandir Road",
            conditionSummary = "Second trimester antenatal checkup, Hb 9.8 g/dL",
            riskLevel = RiskLevel.MODERATE,
            referralStatus = "IFA Distribution Scheduled",
            lastVisited = "Aug 19, 2026",
            phoneNumber = "+91 97654 22109"
        ),
        AshaPatient(
            id = "asha-p4",
            name = "Anand Gokhale",
            age = 65,
            villageArea = "Wakad Gaon",
            conditionSummary = "Diabetic wound dressing review - Stable healing",
            riskLevel = RiskLevel.LOW,
            referralStatus = "Routine Home Check",
            lastVisited = "Aug 16, 2026",
            phoneNumber = "+91 98231 66500"
        )
    )
}
