package com.team.prezel.feature.home.impl.component.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.base.PrezelDropShadowDefaults
import com.team.prezel.core.designsystem.component.base.prezelDropShadow
import com.team.prezel.core.designsystem.theme.PrezelTheme

@LayoutScopeMarker
internal object HomeBottomSheetScope {
    @Composable
    fun Title(
        title: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = title,
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.body2Bold,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Content(
        modifier: Modifier = Modifier,
        verticalArrangement: Arrangement.Vertical = Arrangement.Top,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        contentPadding: PaddingValues = PaddingValues(vertical = PrezelTheme.spacing.V32),
        content: LazyListScope.() -> Unit,
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .prezelDropShadow(style = bottomSheetShadowStyle()),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content,
            contentPadding = contentPadding,
        )
    }

    @Composable
    private fun bottomSheetShadowStyle() =
        PrezelDropShadowDefaults.Custom(
            borderRadius = PrezelTheme.radius.V16,
            backgroundColor = PrezelTheme.colors.bgRegular,
            token = PrezelDropShadowDefaults.PrezelShadowToken(
                offsetX = 0.dp,
                offsetY = (-8).dp,
                blurRadius = 24.dp,
                spreadRadius = 0.dp,
                color = Color(0x14000000),
            ),
        )
}
