package com.team.prezel.core.designsystem.foundation.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

data class PrezelTextStyle(
    val fontWeight: PrezelFontWeight,
    val fontSize: PrezelFontSize,
    val lineHeight: PrezelFontLineHeight,
    val letterSpacing: PrezelFontLetterSpacing,
) {
    @Composable
    fun toTextStyle(): TextStyle =
        TextStyle(
            fontWeight = fontWeight.value,
            fontSize = fontSize.sp,
            lineHeight = lineHeight.sp,
            letterSpacing = letterSpacing.value,
        )
}
