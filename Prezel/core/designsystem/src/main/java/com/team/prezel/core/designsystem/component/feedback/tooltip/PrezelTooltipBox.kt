package com.team.prezel.core.designsystem.component.feedback.tooltip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.compose.balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.rememberBalloonState
import com.skydoves.balloon.compose.setBackgroundColor
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelTooltipBox(
    text: String,
    modifier: Modifier = Modifier,
    showDismissIcon: Boolean = true,
    showArrow: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val builder = rememberBalloonBuilder(showArrow)
    val state = rememberBalloonState(builder)
    val view = LocalView.current
    val visibleFrame = remember { android.graphics.Rect() }
    var shouldRestoreTooltip by remember { mutableStateOf(false) }
    var isAnchorVisibleInWindow by remember { mutableStateOf(true) }

    LaunchedEffect(shouldRestoreTooltip, isAnchorVisibleInWindow) {
        if (shouldRestoreTooltip && isAnchorVisibleInWindow) state.showAlignTop() else state.dismiss()
    }

    Box(
        content = content,
        modifier = modifier
            .noRippleClick { shouldRestoreTooltip = true }
            .onGloballyPositioned { coordinates ->
                view.getWindowVisibleDisplayFrame(visibleFrame)
                isAnchorVisibleInWindow = coordinates.boundsInWindow().intersects(visibleFrame.toComposeRect())
            }.balloon(state) {
                TooltipContent(
                    text = text,
                    showDismissIcon = showDismissIcon,
                    modifier = Modifier.noRippleClick {
                        shouldRestoreTooltip = false
                        state.dismiss()
                    },
                )
            },
    )
}

@Composable
private fun rememberBalloonBuilder(showArrow: Boolean): Balloon.Builder =
    rememberBalloonBuilder {
        setIsVisibleArrow(showArrow)
        setArrowWidth(12)
        setArrowHeight(6)
        setArrowPosition(0.5f)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setCornerRadius(6f)
        setPaddingVertical(4)
        setPaddingLeft(8)
        setPaddingRight(6)
        setDismissWhenTouchOutside(false)
        setBalloonAnimation(BalloonAnimation.NONE)
        setBackgroundColor(PrezelColorScheme.Dark.bgMedium)
    }

private fun Rect.intersects(other: Rect): Boolean = left < other.right && right > other.left && top < other.bottom && bottom > other.top

@Composable
private fun Modifier.noRippleClick(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = null,
        indication = null,
        onClick = onClick,
    )

@Composable
private fun TooltipContent(
    text: String,
    modifier: Modifier = Modifier,
    showDismissIcon: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = PrezelTheme.typography.caption1Regular,
            color = PrezelColorScheme.Dark.textLarge,
        )

        if (showDismissIcon) {
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V2))
            Icon(
                painter = painterResource(PrezelIcons.Cancel),
                contentDescription = stringResource(R.string.core_designsystem_tooltip_cancel_btn_content_desc),
                tint = PrezelColorScheme.Dark.iconLarge,
                modifier = Modifier
                    .size(14.dp)
                    .offset(x = 2.dp),
            )
        }
    }
}

@BasicPreview
@Composable
private fun PrezelTooltipBoxPreview() {
    PrezelTheme {
        PreviewScaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(text = "정확도", style = PrezelTheme.typography.title2Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    PrezelTooltipBox(text = "SPM은 1분당 말하는 음절의 수에요.") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Label", style = PrezelTheme.typography.body3Regular)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(PrezelIcons.Blank),
                                contentDescription = "",
                            )
                        }
                    }

                    PrezelTooltipBox(text = "SPM은 1분당 말하는 음절의 수에요.") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Label", style = PrezelTheme.typography.body3Regular)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(PrezelIcons.Blank),
                                contentDescription = "",
                            )
                        }
                    }

                    PrezelTooltipBox(text = "SPM은 1분당 말하는 음절의 수에요.") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Label", style = PrezelTheme.typography.body3Regular)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(PrezelIcons.Blank),
                                contentDescription = "",
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1000.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "This is Blank for Scroll")
                }
            }
        }
    }
}
