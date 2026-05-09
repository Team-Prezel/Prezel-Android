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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.compose.balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.rememberBalloonState
import com.skydoves.balloon.compose.setBackgroundColor
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

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
        setBackgroundColor(PrezelColorScheme.Dark.bgMedium)
    }

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

    Box(
        content = content,
        modifier = modifier
            .balloon(state) {
                TooltipContent(
                    text = text,
                    showDismissIcon = showDismissIcon,
                )
            }.clickable(
                interactionSource = null,
                indication = null,
                onClick = { state.showAlignTop() },
            ),
    )
}

@Composable
fun TooltipContent(
    text: String,
    showDismissIcon: Boolean = false,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = PrezelTheme.typography.caption1Regular,
            color = PrezelColorScheme.Dark.textLarge,
        )

        if (showDismissIcon) {
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V2))
            Icon(
                modifier = Modifier
                    .size(14.dp)
                    .offset(x = 2.dp),
                painter = painterResource(PrezelIcons.Cancel),
                tint = PrezelColorScheme.Dark.iconLarge,
                contentDescription = "",
            )
        }
    }
}

@BasicPreview
@Composable
private fun PrezelTooltipBoxPreview() {
    PrezelTheme {
        PreviewScaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
