package com.team.prezel.core.network.model.practice

enum class PresentationAnalysisType(
    val value: String,
) {
    EDUCATION("EDUCATION"),
    WORK("WORK"),
    OFFER("OFFER"),
    EVENT("EVENT"),
}

enum class PresentationAnalysisPurpose(
    val value: String,
) {
    INFO("INFO"),
    UNDERSTANDING("UNDERSTANDING"),
    EMPATHY("EMPATHY"),
}

enum class PresentationAnalysisStyle(
    val value: String,
) {
    FORMAL("FORMAL"),
    FRIENDLY("FRIENDLY"),
    CALM("CALM"),
    CASUAL("CASUAL"),
}

enum class PresentationAnalysisAudience(
    val value: String,
) {
    GENERAL("GENERAL"),
    PROFESSIONAL("PROFESSIONAL"),
    TEAMMATE("TEAMMATE"),
}
