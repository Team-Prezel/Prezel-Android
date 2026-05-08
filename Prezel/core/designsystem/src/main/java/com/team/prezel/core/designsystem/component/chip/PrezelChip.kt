package com.team.prezel.core.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipAccent
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipDefaults
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipHierarchy
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
fun PrezelChip(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int? = null,
    type: PrezelChipType = PrezelChipType.FILLED,
    size: PrezelChipSize = PrezelChipSize.REGULAR,
    state: PrezelChipState = PrezelChipState.DEFAULT,
    status: PrezelChipStatus = PrezelChipStatus.DEFAULT,
    hierarchy: PrezelChipHierarchy = PrezelChipHierarchy.PRIMARY,
    accent: PrezelChipAccent = PrezelChipAccent.DEFAULT,
) {
    PrezelChipLayout(
        modifier = modifier,
        text = text,
        iconResId = iconResId,
        style = PrezelChipDefaults.chipStyle(
            type = type,
            size = size,
            state = state,
            status = status,
            hierarchy = hierarchy,
            accent = accent,
        ),
    )
}

private data class ChipPreviewCase(
    val type: PrezelChipType,
    val size: PrezelChipSize = PrezelChipSize.REGULAR,
    val state: PrezelChipState = PrezelChipState.DEFAULT,
    val status: PrezelChipStatus = PrezelChipStatus.DEFAULT,
    val hierarchy: PrezelChipHierarchy = PrezelChipHierarchy.PRIMARY,
    val accent: PrezelChipAccent = PrezelChipAccent.DEFAULT,
    val showLeadingIcon: Boolean = true,
)

private val chipPreviewColumns = listOf(
    PrezelChipSize.SMALL to PrezelChipType.FILLED,
    PrezelChipSize.SMALL to PrezelChipType.OUTLINED,
    PrezelChipSize.REGULAR to PrezelChipType.FILLED,
    PrezelChipSize.REGULAR to PrezelChipType.OUTLINED,
)

private fun previewCases(transform: ChipPreviewCase.() -> ChipPreviewCase = { this }) =
    chipPreviewColumns.map { (size, type) ->
        ChipPreviewCase(type = type, size = size).transform()
    }

@LargeDevicePreview
@Composable
private fun PrezelChipPreview() {
    PreviewSection(
        title = "Chip",
        description = "모든 상태를 하나의 표로 비교합니다. 행은 시나리오, 열은 size/type 조합입니다.",
    ) {
        PreviewMatrix(
            title = "Overview",
            columns = chipPreviewColumns.map { (size, type) ->
                PreviewMatrixColumn(
                    header = "${size.name.lowercase().replaceFirstChar(Char::uppercase)}\n${type.name.lowercase().replaceFirstChar(Char::uppercase)}",
                )
            },
            rows = listOf(
                PreviewMatrixRow(
                    label = "Hierarchy / Primary",
                    values = previewCases(),
                ),
                PreviewMatrixRow(
                    label = "Hierarchy / Secondary",
                    values = previewCases { copy(hierarchy = PrezelChipHierarchy.SECONDARY) },
                ),
                PreviewMatrixRow(
                    label = "State / Active",
                    values = previewCases { copy(state = PrezelChipState.ACTIVE) },
                ),
                PreviewMatrixRow(
                    label = "Status / Bad",
                    values = previewCases { copy(status = PrezelChipStatus.BAD) },
                ),
                PreviewMatrixRow(
                    label = "Status / Warning",
                    values = previewCases { copy(status = PrezelChipStatus.WARNING) },
                ),
                PreviewMatrixRow(
                    label = "Accent / Purple",
                    values = previewCases { copy(accent = PrezelChipAccent.PURPLE) },
                ),
                PreviewMatrixRow(
                    label = "Accent / Teal",
                    values = previewCases { copy(accent = PrezelChipAccent.TEAL) },
                ),
                PreviewMatrixRow(
                    label = "Leading Icon / Off",
                    values = previewCases { copy(showLeadingIcon = false) },
                ),
                PreviewMatrixRow(
                    label = "Leading Icon / On",
                    values = previewCases(),
                ),
            ),
        ) { previewCase ->
            PreviewChip(previewCase)
        }
    }
}

@Composable
private fun PreviewChip(previewCase: ChipPreviewCase) {
    PrezelChip(
        text = "Label",
        iconResId = if (previewCase.showLeadingIcon) PrezelIcons.Blank else null,
        type = previewCase.type,
        size = previewCase.size,
        state = previewCase.state,
        status = previewCase.status,
        hierarchy = previewCase.hierarchy,
        accent = previewCase.accent,
    )
}
