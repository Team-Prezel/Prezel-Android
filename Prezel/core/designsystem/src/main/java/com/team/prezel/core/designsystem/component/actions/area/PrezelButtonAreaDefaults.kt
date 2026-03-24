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
     * 배경 노출 여부와 중첩 여부에 맞는 기본 스타일을 반환합니다.
     */
    @Composable
    fun getDefault(
        showBackground: Boolean = false,
        isNested: Boolean = false,
        backgroundColor: Color = getBackgroundColor(showBackground = showBackground),
        borderColor: Color = getBorderColor(showBackground = showBackground),
        contentPadding: PaddingValues = getContentPadding(isNested = isNested),
    ): PrezelButtonAreaDefault =
        PrezelButtonAreaDefault(
            backgroundColor = backgroundColor,
            borderColor = borderColor,
            contentPadding = contentPadding,
        )

    @Composable
    private fun getBackgroundColor(showBackground: Boolean): Color = if (showBackground) PrezelTheme.colors.bgRegular else Color.Transparent

    @Composable
    private fun getBorderColor(showBackground: Boolean): Color = if (showBackground) PrezelTheme.colors.borderRegular else Color.Transparent

    @Composable
    private fun getContentPadding(isNested: Boolean): PaddingValues = PaddingValues(if (isNested) PrezelTheme.spacing.V0 else PrezelTheme.spacing.V20)
}
