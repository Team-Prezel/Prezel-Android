package com.team.prezel.core.model.badge

data class Badge(
    val type: BadgeType,
    val isAchieved: Boolean,
)

enum class BadgeType {
    FIRST_PRESENTATION,
    SECOND_ANALYSIS,
    FIRST_PRACTICE,
    RETROSPECT_COMPLETED,
    PERFECT_SCORE,
    TEN_ANALYSIS,
}
