package com.team.prezel.feature.home.impl.main.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Curation

@Immutable
internal data class CurationUiModel(
    val guideMessage: String,
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
                materialType = materialType,
                title = title,
                sourceChannel = sourceChannel,
                linkUrl = linkUrl,
                imageUrl = imageUrl,
            )
    }
}
