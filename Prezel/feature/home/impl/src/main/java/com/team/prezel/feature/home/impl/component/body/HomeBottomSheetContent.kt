package com.team.prezel.feature.home.impl.component.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.base.PrezelDropShadowDefaults
import com.team.prezel.core.designsystem.component.base.prezelDropShadow
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun HomeBottomSheetContent(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentPadding: PaddingValues = PaddingValues(vertical = PrezelTheme.spacing.V32),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .prezelDropShadow(style = bottomSheetShadowStyle())
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}

@Composable
private fun bottomSheetShadowStyle() =
    PrezelDropShadowDefaults.Custom(
        borderRadius = PrezelTheme.radius.V16,
        backgroundColor = PrezelTheme.colors.bgRegular,
        token = PrezelDropShadowDefaults.PrezelShadowToken(
            offsetX = 0.dp,
            offsetY = (-6).dp,
            blurRadius = 12.dp,
            spreadRadius = 0.dp,
            color = Color(0xFFF5F6F7),
        ),
    )

@BasicPreview
@Composable
private fun HomeBottomSheetContentPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(top = 16.dp),
        ) {
            HomeBottomSheetContent(
                contentPadding = PaddingValues(
                    vertical = PrezelTheme.spacing.V32,
                    horizontal = PrezelTheme.spacing.V20,
                ),
                verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
            ) {
                HomeBottomSheetTitle(title = "지금부터 연습해보세요")
                repeat(3) { index ->
                    Text(
                        text = "${index + 1}. 발표 흐름을 다시 점검해보세요",
                        color = PrezelTheme.colors.textRegular,
                        style = PrezelTheme.typography.body3Regular,
                    )
                }
            }
        }
    }
}
