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
    V100(12.dp),
    V200(14.dp),
    V300(16.dp),
    V400(20.dp),
    V500(24.dp),
    ;

    val sp: TextUnit
        @Composable
        get() = with(LocalDensity.current) { value.toSp() }
}

enum class PrezelFontLineHeight(
    val value: Dp,
) {
    V100(16.dp),
    V150(18.dp),
    V200(20.dp),
    V300(24.dp),
    V400(28.dp),
    V500(32.dp),
    ;

    val sp: TextUnit
        @Composable
        get() = with(LocalDensity.current) { value.toSp() }
}

enum class PrezelFontLetterSpacing(
    val value: TextUnit,
) {
    V100(0.012.em),
    V200(0.008.em),
    V300(0.006.em),
    V400((-0.012).em),
    V500((-0.018).em),
}
