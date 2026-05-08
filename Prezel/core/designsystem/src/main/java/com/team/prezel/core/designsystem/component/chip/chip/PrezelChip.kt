package com.team.prezel.core.designsystem.component.chip.chip

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.chip.base.PrezelChipLayout
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.LargeDevicePreview
import com.team.prezel.core.designsystem.preview.PreviewMatrix
import com.team.prezel.core.designsystem.preview.PreviewMatrixColumn
import com.team.prezel.core.designsystem.preview.PreviewMatrixRow
import com.team.prezel.core.designsystem.preview.PreviewSection

@Immutable
enum class ChipType {
    FILLED,
    OUTLINED,
}

@Immutable
enum class ChipSize {
    SMALL,
    REGULAR,
}

@Immutable
enum class ChipState {
    DEFAULT,
    ACTIVE,
}

@Immutable
enum class ChipStatus {
    DEFAULT,
    BAD,
}

@Immutable
enum class ChipHierarchy {
    PRIMARY,
    SECONDARY,
}

@Immutable
enum class ChipAccent {
    DEFAULT,
    WARNING,
    PURPLE,
    TEAL,
}

@Composable
fun PrezelChip(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int? = null,
    type: ChipType = ChipType.FILLED,
    size: ChipSize = ChipSize.REGULAR,
    state: ChipState = ChipState.DEFAULT,
    status: ChipStatus = ChipStatus.DEFAULT,
    hierarchy: ChipHierarchy = ChipHierarchy.PRIMARY,
    accent: ChipAccent = ChipAccent.DEFAULT,
) {
    PrezelChipLayout(
        modifier = modifier,
        text = text,
        iconResId = iconResId,
        style = PrezelChipDefaults.getDefault(
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
    val type: ChipType,
    val size: ChipSize = ChipSize.REGULAR,
    val state: ChipState = ChipState.DEFAULT,
    val status: ChipStatus = ChipStatus.DEFAULT,
    val hierarchy: ChipHierarchy = ChipHierarchy.PRIMARY,
    val accent: ChipAccent = ChipAccent.DEFAULT,
    val showLeadingIcon: Boolean = true,
)

private val chipPreviewColumns = listOf(
    ChipSize.SMALL to ChipType.FILLED,
    ChipSize.SMALL to ChipType.OUTLINED,
    ChipSize.REGULAR to ChipType.FILLED,
    ChipSize.REGULAR to ChipType.OUTLINED,
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
                    values = previewCases { copy(hierarchy = ChipHierarchy.SECONDARY) },
                ),
                PreviewMatrixRow(
                    label = "State / Active",
                    values = previewCases { copy(state = ChipState.ACTIVE) },
                ),
                PreviewMatrixRow(
                    label = "Status / Bad",
                    values = previewCases { copy(status = ChipStatus.BAD) },
                ),
                PreviewMatrixRow(
                    label = "Accent / Purple",
                    values = previewCases { copy(accent = ChipAccent.PURPLE) },
                ),
                PreviewMatrixRow(
                    label = "Accent / Teal",
                    values = previewCases { copy(accent = ChipAccent.TEAL) },
                ),
                PreviewMatrixRow(
                    label = "Accent / Warning",
                    values = previewCases { copy(accent = ChipAccent.WARNING) },
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
