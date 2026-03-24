package com.team.prezel.core.designsystem.component.actions.button.floating.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

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

@ThemePreview
@Composable
private fun PrezelMenuPreview() {
    PrezelTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(Color.LightGray)
                .padding(8.dp),
        ) {
            PrezelMenu {
                repeat(5) {
                    MenuItem(
                        label = "Label",
                        iconResId = PrezelIcons.Blank,
                        onClick = {},
                    )
                }
            }

            PrezelMenu(size = MenuSize.SMALL) {
                repeat(5) {
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
