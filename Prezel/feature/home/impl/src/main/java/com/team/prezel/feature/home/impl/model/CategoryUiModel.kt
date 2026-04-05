package com.team.prezel.feature.home.impl.model

import androidx.annotation.DrawableRes
import com.team.prezel.core.designsystem.R

enum class CategoryUiModel(
    val label: String,
) {
    PERSUASION("설득·제안"),
    EVENT("행사·공개"),
    EDUCATION("학술·교육"),
    REPORT("업무·보고"),
}

@DrawableRes
fun CategoryUiModel.backgroundRes(): Int =
    when (this) {
        CategoryUiModel.PERSUASION -> R.drawable.core_designsystem_section_title_hand
        CategoryUiModel.EVENT -> R.drawable.core_designsystem_section_title_event
        CategoryUiModel.EDUCATION -> R.drawable.core_designsystem_section_title_college
        CategoryUiModel.REPORT -> R.drawable.core_designsystem_section_title_company
    }
