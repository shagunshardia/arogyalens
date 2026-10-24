package com.example.data

object Localization {
    fun getGreeting(lang: AppLanguage, name: String): String {
        return when (lang) {
            AppLanguage.ENGLISH -> "Good morning, $name 👋"
            AppLanguage.HINDI -> "नमस्ते, $name 👋"
            AppLanguage.MARATHI -> "शुभ प्रभात, $name 👋"
        }
    }

    fun getSubtitle(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ENGLISH -> "How can we help you today?"
            AppLanguage.HINDI -> "आज हम आपकी क्या मदद कर सकते हैं?"
            AppLanguage.MARATHI -> "आज आम्ही तुम्हाला कशी मदत करू शकतो?"
        }
    }

    fun getOfflineStatus(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ENGLISH -> "Offline Mode Active"
            AppLanguage.HINDI -> "ऑफलाइन मोड सक्रिय"
            AppLanguage.MARATHI -> "ऑफलाइन मोड सक्रिय"
        }
    }

    fun getOfflineSub(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ENGLISH -> "Your health records & basic screening tools are available offline."
            AppLanguage.HINDI -> "आपके स्वास्थ्य रिकॉर्ड और बुनियादी जांच ऑफलाइन उपलब्ध हैं।"
            AppLanguage.MARATHI -> "तुमचे आरोग्य नोंदी आणि प्राथमिक तपासणी ऑफलाइन उपलब्ध आहेत."
        }
    }

    fun getRiskText(lang: AppLanguage, risk: RiskLevel): String {
        return when (risk) {
            RiskLevel.LOW -> when (lang) {
                AppLanguage.ENGLISH -> "LOW RISK"
                AppLanguage.HINDI -> "कमी जोखीम (LOW RISK)"
                AppLanguage.MARATHI -> "कमी जोखीम (LOW RISK)"
            }
            RiskLevel.MODERATE -> when (lang) {
                AppLanguage.ENGLISH -> "MODERATE RISK"
                AppLanguage.HINDI -> "मध्यम जोखीम (MODERATE)"
                AppLanguage.MARATHI -> "मध्यम जोखीम (MODERATE)"
            }
            RiskLevel.HIGH -> when (lang) {
                AppLanguage.ENGLISH -> "HIGH RISK"
                AppLanguage.HINDI -> "उच्च जोखीम (HIGH RISK)"
                AppLanguage.MARATHI -> "उच्च जोखीम (HIGH RISK)"
            }
        }
    }
}
