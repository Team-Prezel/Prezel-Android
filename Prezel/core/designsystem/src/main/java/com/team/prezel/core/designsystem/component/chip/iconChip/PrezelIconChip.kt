package com.team.prezel.core.designsystem.component.chip.iconChip

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.team.prezel.core.designsystem.component.chip.base.PrezelChipLayout
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.LargeDevicePreview
import com.team.prezel.core.designsystem.preview.PreviewMatrix
import com.team.prezel.core.designsystem.preview.PreviewMatrixColumn
import com.team.prezel.core.designsystem.preview.PreviewMatrixRow
import com.team.prezel.core.designsystem.preview.PreviewSection

@Immutable
enum class IconChipType {
    FILLED,
    OUTLINED,
}

@Immutable
enum class IconChipSize {
    SMALL,
    REGULAR,
}

@Immutable
enum class IconChipState {
    DEFAULT,
    ACTIVE,
    DISABLED,
}

@Immutable
enum class IconChipStatus {
    DEFAULT,
    BAD,
}

@Composable
fun PrezelIconChip(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    type: IconChipType = IconChipType.FILLED,
    size: IconChipSize = IconChipSize.REGULAR,
    state: IconChipState = IconChipState.DEFAULT,
    status: IconChipStatus = IconChipStatus.DEFAULT,
) {
    PrezelChipLayout(
        modifier = if (contentDescription != null) {
            modifier.semantics { this.contentDescription = contentDescription }
        } else {
            modifier
        },
        text = null,
        iconResId = iconResId,
        style = PrezelIconChipDefaults.getDefault(
            type = type,
            size = size,
            state = state,
            status = status,
        ),
    )
}

private data class IconChipPreviewCase(
    val type: IconChipType,
    val size: IconChipSize = IconChipSize.REGULAR,
    val state: IconChipState = IconChipState.DEFAULT,
    val status: IconChipStatus = IconChipStatus.DEFAULT,
)

private val iconChipPreviewColumns = listOf(
    IconChipSize.SMALL to IconChipType.FILLED,
    IconChipSize.SMALL to IconChipType.OUTLINED,
    IconChipSize.REGULAR to IconChipType.FILLED,
    IconChipSize.REGULAR to IconChipType.OUTLINED,
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
                    values = iconPreviewCases { copy(state = IconChipState.ACTIVE) },
                ),
                PreviewMatrixRow(
                    label = "State / Disabled",
                    values = iconPreviewCases { copy(state = IconChipState.DISABLED) },
                ),
                PreviewMatrixRow(
                    label = "Status / Bad",
                    values = iconPreviewCases { copy(status = IconChipStatus.BAD) },
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
