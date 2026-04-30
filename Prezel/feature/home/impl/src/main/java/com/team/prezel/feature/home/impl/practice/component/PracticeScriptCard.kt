package com.team.prezel.feature.home.impl.practice.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PracticeScriptCard(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(PrezelTheme.radius.V6))
            .background(PrezelTheme.colors.bgMedium)
            .padding(horizontal = PrezelTheme.spacing.V16, vertical = PrezelTheme.spacing.V12),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = PrezelTheme.typography.body2Regular,
            color = PrezelTheme.colors.textLarge,
            textAlign = TextAlign.Center,
        )
    }
}
