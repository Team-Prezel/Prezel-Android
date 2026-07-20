package com.team.prezel.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

private val TopAppBarHeight = 56.dp

@LayoutScopeMarker
class PrezelTopAppBarScope {
    internal var leadingIcon: TopAppBarIcon? = null
    internal val trailingContents = mutableListOf<TopAppBarTrailingContent>()

    @Composable
    fun LeadingIcon(
        @DrawableRes iconResId: Int,
        contentDescription: String?,
        onClick: () -> Unit,
    ) {
        leadingIcon = TopAppBarIcon(
            iconResId = iconResId,
            contentDescription = contentDescription,
            onClick = onClick,
        )
    }

    @Composable
    fun TrailingIcon(
        @DrawableRes iconResId: Int,
        contentDescription: String?,
        onClick: () -> Unit,
    ) {
        trailingContents += TopAppBarTrailingContent.Icon(
            iconResId = iconResId,
            contentDescription = contentDescription,
            onClick = onClick,
        )
    }

    @Composable
    fun TrailingButton(
        label: String,
        onClick: () -> Unit,
    ) {
        trailingContents += TopAppBarTrailingContent.Button(
            label = label,
            onClick = onClick,
        )
    }
}

@Immutable
internal data class TopAppBarIcon(
    @param:DrawableRes val iconResId: Int,
    val contentDescription: String?,
    val onClick: () -> Unit,
)

@Immutable
internal sealed interface TopAppBarTrailingContent {
    data class Icon(
        @param:DrawableRes val iconResId: Int,
        val contentDescription: String?,
        val onClick: () -> Unit,
    ) : TopAppBarTrailingContent

    data class Button(
        val label: String,
        val onClick: () -> Unit,
    ) : TopAppBarTrailingContent
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrezelTopAppBar(
    title: String?,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    content: @Composable PrezelTopAppBarScope.() -> Unit = {},
) {
    val scope = PrezelTopAppBarScope().apply { content() }
    val containerColor =
        animateColorAsState(
            targetValue = if (scrollBehavior?.state?.overlappedFraction?.let { it > 0f } == true) {
                PrezelTheme.colors.bgRegular
            } else {
                Color.Transparent
            },
            animationSpec = tween(),
            label = "PrezelTopAppBarContainerColor",
        )

    CompositionLocalProvider(LocalContentColor provides PrezelTheme.colors.iconRegular) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(TopAppBarHeight)
                .background(color = containerColor.value),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            scope.leadingIcon?.let { icon ->
                Spacer(modifier = Modifier.width(PrezelTheme.spacing.V8))
                PrezelTopAppBarLeadingIcon(icon = icon)
                Spacer(modifier = Modifier.width(PrezelTheme.spacing.V4))
            }

            if (scope.leadingIcon == null) Spacer(modifier = Modifier.width(PrezelTheme.spacing.V20))
            Text(
                text = title.orEmpty(),
                style = PrezelTheme.typography.body2Bold,
                color = PrezelTheme.colors.textLarge,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V4))

            Row(verticalAlignment = Alignment.CenterVertically) {
                scope.trailingContents.forEach { trailingContent ->
                    when (trailingContent) {
                        is TopAppBarTrailingContent.Button -> PrezelTopAppBarTrailingButton(trailingContent = trailingContent)
                        is TopAppBarTrailingContent.Icon -> PrezelTopAppBarTrailingIcon(trailingContent = trailingContent)
                    }
                }
            }
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V8))
        }
    }
}

@Composable
private fun PrezelTopAppBarLeadingIcon(icon: TopAppBarIcon) {
    IconButton(
        onClick = icon.onClick,
        shape = PrezelTheme.shapes.V8,
        modifier = Modifier.size(48.dp),
    ) {
        Icon(
            painter = painterResource(icon.iconResId),
            contentDescription = icon.contentDescription,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun PrezelTopAppBarTrailingIcon(trailingContent: TopAppBarTrailingContent.Icon) {
    IconButton(
        onClick = trailingContent.onClick,
        shape = PrezelTheme.shapes.V8,
        modifier = Modifier.size(48.dp),
    ) {
        Icon(
            painter = painterResource(trailingContent.iconResId),
            contentDescription = trailingContent.contentDescription,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun PrezelTopAppBarTrailingButton(trailingContent: TopAppBarTrailingContent.Button) {
    PrezelButton(
        text = trailingContent.label,
        type = ButtonType.GHOST,
        hierarchy = ButtonHierarchy.SECONDARY,
        size = ButtonSize.SMALL,
        isRounded = false,
        onClick = trailingContent.onClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@BasicPreview
@Composable
private fun PrezelTopAppBarTitleOnlyPreview() {
    PreviewSection(title = "Title Only") {
        PrezelTopAppBar(title = "제목")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@BasicPreview
@Composable
private fun PrezelTopAppBarWithLeadingPreview() {
    PreviewSection(title = "With Leading") {
        PrezelTopAppBar(
            title = "제목",
        ) {
            LeadingIcon(
                iconResId = PrezelIcons.Blank,
                contentDescription = "뒤로가기",
                onClick = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@BasicPreview
@Composable
private fun PrezelTopAppBarWithAllIconsPreview() {
    PreviewSection(title = "With All Icons") {
        PrezelTopAppBar(
            title = "Title",
        ) {
            LeadingIcon(
                iconResId = PrezelIcons.Blank,
                contentDescription = "뒤로가기",
                onClick = {},
            )
            TrailingIcon(
                iconResId = PrezelIcons.Blank,
                contentDescription = "검색",
                onClick = {},
            )
            TrailingButton(
                label = "Label",
                onClick = {},
            )
        }
    }
}
