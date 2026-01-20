package com.team.prezel.core.designsystem.foundation.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class PrezelFontWeight(
    val value: FontWeight,
) {
    REGULAR(FontWeight.Medium),
    MEDIUM(FontWeight.SemiBold),
    BOLD(FontWeight.Bold),
}

enum class PrezelFontSize(
    private val value: Dp,
) {
    V_100(12.dp),
    V_200(14.dp),
    V_300(16.dp),
    V_400(20.dp),
    V_500(24.dp),
    ;

    @Composable
    fun sp(): TextUnit {
        val density = LocalDensity.current
        return with(density) {
            (value.toPx() / fontScale).sp
        }
    }
}

enum class PrezelFontLineHeight(
    val value: TextUnit,
) {
    V_100(16.sp),
    V_150(18.sp),
    V_200(20.sp),
    V_300(24.sp),
    V_400(28.sp),
    V_500(32.sp),
}

enum class PrezelFontLetterSpacing(
    val value: Float,
) {
    V_100(1.2f),
    V_200(0.8f),
    V_300(0.6f),
    V_400(-1.2f),
    V_500(-1.8f),
    ;

    fun multiply(fontSize: TextUnit): TextUnit = fontSize * value
}
