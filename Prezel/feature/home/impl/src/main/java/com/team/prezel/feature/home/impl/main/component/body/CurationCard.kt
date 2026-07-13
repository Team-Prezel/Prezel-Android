package com.team.prezel.feature.home.impl.main.component.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelAsyncImage
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.ChipType
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.home.impl.main.model.CurationUiModel

@Composable
internal fun CurationCard(
    curation: CurationUiModel,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = { uriHandler.openUri(curation.linkUrl) }),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
    ) {
        Box(
            modifier = Modifier
                .size(width = 152.dp, height = 86.dp)
                .clip(PrezelTheme.shapes.V4),
        ) {
            PrezelAsyncImage(
                url = curation.imageUrl,
                contentDescription = curation.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            PrezelChip(
                text = curation.materialType,
                modifier = Modifier.padding(PrezelTheme.spacing.V4),
                type = ChipType.FILLED,
                size = ChipSize.SMALL,
            )
        }

        Column(
            modifier = Modifier.height(86.dp),
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V4),
        ) {
            Text(
                text = curation.title,
                color = PrezelTheme.colors.textLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = PrezelTheme.typography.caption1Medium,
            )
            Text(
                text = curation.sourceChannel,
                color = PrezelTheme.colors.textSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = PrezelTheme.typography.caption2Regular,
            )
        }
    }
}

@BasicPreview
@Composable
private fun CurationCardPreview() {
    PrezelTheme {
        CurationCard(
            curation = CurationUiModel(
                guideMessage = "발표 흐름을 키워드별로 정리해보세요",
                materialType = "아티클",
                title = "제목이 두 줄로 넘어가면 자연스럽게 말줄임표로 전환돼요",
                sourceChannel = "계정 이름",
                linkUrl = "https://example.com",
                imageUrl = "",
            ),
        )
    }
}
