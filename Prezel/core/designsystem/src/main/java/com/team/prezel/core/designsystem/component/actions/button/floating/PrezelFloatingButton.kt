package com.team.prezel.core.designsystem.component.actions.button.floating

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.base.PrezelDropShadowDefaults
import com.team.prezel.core.designsystem.component.base.prezelDropShadow
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 확장 상태에 따라 아이콘이 바뀌는 플로팅 액션 버튼입니다.
 */
@Composable
fun PrezelFloatingButton(
    isExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.REGULAR,
    hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
    @DrawableRes openIconResId: Int = PrezelIcons.Cancel,
) {
    PrezelIconButton(
        iconResId = if (isExpanded) openIconResId else iconResId,
        size = size,
        hierarchy = hierarchy,
        isRounded = true,
        modifier = modifier.prezelDropShadow(
            style = PrezelDropShadowDefaults.Regular(
                borderRadius = PrezelTheme.radius.V1000,
            ),
        ),
        onClick = { onChangeExpanded(!isExpanded) },
    )
}

@BasicPreview
@Composable
private fun PrezelFloatingButtonPreview() {
    var expanded by remember { mutableStateOf(false) }

    PreviewSection(
        title = "Floating Button",
        description = "Floating Button은 아이콘으로 공통 기능을 안내합니다.",
    ) {
        ButtonHierarchy.entries.forEach { hierarchy ->
            ButtonSize.entries.forEach { size ->
                PreviewValueRow(
                    name = hierarchy.name,
                    valueLabel = size.name,
                ) {
                    PrezelFloatingButton(
                        isExpanded = expanded,
                        onChangeExpanded = { expanded = it },
                        iconResId = PrezelIcons.Blank,
                        hierarchy = hierarchy,
                        size = size,
                    )
                }
            }
        }
    }
}
