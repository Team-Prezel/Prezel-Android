package com.team.prezel.core.designsystem.component.actions.button.floating

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.floating.menu.MenuSize
import com.team.prezel.core.designsystem.component.actions.button.floating.menu.PrezelMenu
import com.team.prezel.core.designsystem.component.actions.button.floating.menu.PrezelMenuScope
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 확장 가능한 메뉴와 토글 버튼을 함께 배치하는 플로팅 액션 메뉴입니다.
 */
@Composable
fun PrezelFloatingMenu(
    isExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    @DrawableRes openIconResId: Int = PrezelIcons.Cancel,
    size: ButtonSize = ButtonSize.REGULAR,
    hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
    items: @Composable PrezelMenuScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        if (isExpanded) {
            PrezelMenu(
                size = when (size) {
                    ButtonSize.SMALL -> MenuSize.SMALL
                    ButtonSize.REGULAR -> MenuSize.REGULAR
                    else -> MenuSize.REGULAR
                },
                content = items,
            )
        }

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        PrezelFloatingButton(
            isExpanded = isExpanded,
            onChangeExpanded = onChangeExpanded,
            iconResId = iconResId,
            openIconResId = openIconResId,
            size = size,
            hierarchy = hierarchy,
        )
    }
}

@BasicPreview
@Composable
private fun PrezelFloatingMenuPreview() {
    var expanded by remember { mutableStateOf(true) }

    PreviewSection(
        title = "Floating Menu",
        description = "확장된 메뉴와 토글 버튼의 배치를 확인합니다.",
    ) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .padding(8.dp),
        ) {
            PrezelFloatingMenu(
                isExpanded = expanded,
                onChangeExpanded = { expanded = it },
                iconResId = PrezelIcons.Blank,
                hierarchy = ButtonHierarchy.PRIMARY,
                size = ButtonSize.REGULAR,
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                repeat(5) {
                    MenuItem(
                        label = "Label",
                        iconResId = PrezelIcons.Blank,
                        onClick = { expanded = !expanded },
                    )
                }
            }
        }
    }
}
