package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipDefaults
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipLayout
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipSize
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipState
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipStatus
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.LargeDevicePreview
import com.team.prezel.core.designsystem.preview.PreviewMatrix
import com.team.prezel.core.designsystem.preview.PreviewMatrixColumn
import com.team.prezel.core.designsystem.preview.PreviewMatrixRow
import com.team.prezel.core.designsystem.preview.PreviewSection

@Composable
fun PrezelIconChip(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    type: PrezelChipType = PrezelChipType.FILLED,
    size: PrezelChipSize = PrezelChipSize.REGULAR,
    state: PrezelChipState = PrezelChipState.DEFAULT,
    status: PrezelChipStatus = PrezelChipStatus.DEFAULT,
) {
    PrezelChipLayout(
        modifier = modifier,
        text = null,
        iconResId = iconResId,
        style = PrezelChipDefaults.iconChipStyle(
            type = type,
            size = size,
            state = state,
            status = status,
        ),
    )
}

private data class IconChipPreviewCase(
    val type: PrezelChipType,
    val size: PrezelChipSize = PrezelChipSize.REGULAR,
    val state: PrezelChipState = PrezelChipState.DEFAULT,
    val status: PrezelChipStatus = PrezelChipStatus.DEFAULT,
)

private val iconChipPreviewColumns = listOf(
    PrezelChipSize.SMALL to PrezelChipType.FILLED,
    PrezelChipSize.SMALL to PrezelChipType.OUTLINED,
    PrezelChipSize.REGULAR to PrezelChipType.FILLED,
    PrezelChipSize.REGULAR to PrezelChipType.OUTLINED,
)

private fun iconPreviewCases(transform: IconChipPreviewCase.() -> IconChipPreviewCase = { this }) =
    iconChipPreviewColumns.map { (size, type) ->
        IconChipPreviewCase(type = type, size = size).transform()
    }

@LargeDevicePreview
@Composable
private fun PrezelIconChipPreview() {
    PreviewSection(
        title = "Icon Chip",
        description = "icon-only 케이스를 하나의 표로 비교합니다. 행은 시나리오, 열은 size/type 조합입니다.",
    ) {
        PreviewMatrix(
            title = "Overview",
            columns = iconChipPreviewColumns.map { (size, type) ->
                PreviewMatrixColumn(
                    header = "${size.name.lowercase().replaceFirstChar(Char::uppercase)}\n${type.name.lowercase().replaceFirstChar(Char::uppercase)}",
                )
            },
            rows = listOf(
                PreviewMatrixRow(
                    label = "Default",
                    values = iconPreviewCases(),
                ),
                PreviewMatrixRow(
                    label = "State / Active",
                    values = iconPreviewCases { copy(state = PrezelChipState.ACTIVE) },
                ),
                PreviewMatrixRow(
                    label = "State / Disabled",
                    values = iconPreviewCases { copy(state = PrezelChipState.DISABLED) },
                ),
                PreviewMatrixRow(
                    label = "Status / Bad",
                    values = iconPreviewCases { copy(status = PrezelChipStatus.BAD) },
                ),
                PreviewMatrixRow(
                    label = "Status / Warning",
                    values = iconPreviewCases { copy(status = PrezelChipStatus.WARNING) },
                ),
            ),
        ) { previewCase ->
            PreviewIconChip(previewCase)
        }
    }
}

@Composable
private fun PreviewIconChip(previewCase: IconChipPreviewCase) {
    PrezelIconChip(
        iconResId = PrezelIcons.Blank,
        type = previewCase.type,
        size = previewCase.size,
        state = previewCase.state,
        status = previewCase.status,
    )
}
