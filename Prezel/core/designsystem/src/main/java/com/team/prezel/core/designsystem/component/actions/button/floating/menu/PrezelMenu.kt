package com.team.prezel.core.designsystem.component.actions.button.floating.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow

/**
 * 플로팅 액션 메뉴 항목을 세로로 배치하는 컨테이너입니다.
 */
@Composable
fun PrezelMenu(
    modifier: Modifier = Modifier,
    size: MenuSize = MenuSize.REGULAR,
    config: PrezelMenuDefault = PrezelMenuDefaults.getDefault(size),
    content: @Composable PrezelMenuScope.() -> Unit,
) {
    val scope = remember(size) { DefaultPrezelMenuScope(menuSize = size) }

    Column(
        modifier = modifier
            .clip(shape = config.shape)
            .background(color = config.backgroundColor)
            .padding(config.contentPadding),
        verticalArrangement = config.verticalArrangement,
    ) {
        scope.content()
    }
}

@BasicPreview
@Composable
private fun PrezelMenuPreview() {
    PreviewSection(
        title = "Floating Menu Container",
        description = "Regular/Small 메뉴 컨테이너의 크기와 간격을 비교합니다.",
    ) {
        MenuSize.entries.forEach { size ->
            PreviewValueRow(name = size.name) {
                PrezelMenu(
                    size = size,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp),
                ) {
                    repeat(3) {
                        MenuItem(
                            label = "Label",
                            iconResId = PrezelIcons.Blank,
                            onClick = {},
                        )
                    }
                }
            }
        }
    }
}
