package com.team.prezel.core.designsystem.component.actions.area

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 버튼 영역의 배경, 구분선, 내부 여백을 묶은 스타일 값입니다.
 */
@Immutable
data class PrezelButtonAreaDefault(
    val backgroundColor: Color,
    val borderColor: Color,
    val contentPadding: PaddingValues,
)

/**
 * [PrezelButtonArea]의 기본 스타일 값을 계산합니다.
 */
object PrezelButtonAreaDefaults {
    /**
     * 버튼 영역에 사용할 기본 스타일을 반환합니다.
     */
    @Composable
    fun getDefault(
        backgroundColor: Color = getBackgroundColor(),
        borderColor: Color = getBorderColor(),
        contentPadding: PaddingValues = getContentPadding(),
    ): PrezelButtonAreaDefault =
        PrezelButtonAreaDefault(
            backgroundColor = backgroundColor,
            borderColor = borderColor,
            contentPadding = contentPadding,
        )

    @Composable
    private fun getBackgroundColor(): Color = PrezelTheme.colors.bgRegular

    @Composable
    private fun getBorderColor(): Color = PrezelTheme.colors.borderRegular

    @Composable
    private fun getContentPadding(): PaddingValues = PaddingValues(PrezelTheme.spacing.V20)
}
