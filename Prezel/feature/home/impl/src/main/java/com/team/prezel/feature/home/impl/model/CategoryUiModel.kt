package com.team.prezel.feature.home.impl.model

import androidx.annotation.DrawableRes
import com.team.prezel.core.designsystem.icon.PrezelIcons

enum class CategoryUiModel(
    val label: String,
    @param:DrawableRes val iconResId: Int,
) {
    PERSUASION(
        label = "설득·제안",
        iconResId = PrezelIcons.Hand,
    ),
    EVENT(
        label = "행사·공개",
        iconResId = PrezelIcons.Balloon,
    ),
    EDUCATION(
        label = "학술·교육",
        iconResId = PrezelIcons.College,
    ),
    REPORT(
        label = "업무·보고",
        iconResId = PrezelIcons.Company,
    ),
}
