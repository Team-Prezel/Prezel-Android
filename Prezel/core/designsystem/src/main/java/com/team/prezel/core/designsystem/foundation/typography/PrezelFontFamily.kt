package com.team.prezel.core.designsystem.foundation.typography

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.team.prezel.core.designsystem.R

internal val SuitFamily = FontFamily(
    Font(R.font.suit_medium, PrezelFontWeight.REGULAR.value),
    Font(R.font.suit_semi_bold, PrezelFontWeight.MEDIUM.value),
    Font(R.font.suit_bold, PrezelFontWeight.BOLD.value),
)
