package com.team.prezel.feature.home.impl.main.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Curation
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style

@Immutable
internal data class CurationUiModel(
    val guideMessage: String,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val materialType: String,
    val title: String,
    val sourceChannel: String,
    val linkUrl: String,
    val imageUrl: String,
) {
    companion object {
        fun Curation.toUiModel(): CurationUiModel =
            CurationUiModel(
                guideMessage = guideMessage,
                category = category,
                purpose = purpose,
                style = style,
                audience = audience,
                materialType = materialType,
                title = title,
                sourceChannel = sourceChannel,
                linkUrl = linkUrl,
                imageUrl = imageUrl,
            )
    }
}
