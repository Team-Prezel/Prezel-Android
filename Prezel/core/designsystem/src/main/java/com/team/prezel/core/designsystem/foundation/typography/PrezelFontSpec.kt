package com.team.prezel.core.designsystem.foundation.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

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

    val sp: TextUnit
        @Composable
        get() = with(LocalDensity.current) { value.toSp() }
}

enum class PrezelFontLineHeight(
    val value: Dp,
) {
    V_100(16.dp),
    V_150(18.dp),
    V_200(20.dp),
    V_300(24.dp),
    V_400(28.dp),
    V_500(32.dp),
    ;

    val sp: TextUnit
        @Composable
        get() = with(LocalDensity.current) { value.toSp() }
}

enum class PrezelFontLetterSpacing(
    val value: TextUnit,
) {
    V_100(0.012.em),
    V_200(0.008.em),
    V_300(0.006.em),
    V_400((-0.012).em),
    V_500((-0.018).em),
}
