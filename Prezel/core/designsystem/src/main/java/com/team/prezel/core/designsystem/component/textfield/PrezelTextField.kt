package com.team.prezel.core.designsystem.component.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldLabel
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldPlaceholder
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldSupportingText
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewColumn
import com.team.prezel.core.designsystem.preview.PreviewSurface
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    feedback: PrezelTextFieldFeedback = PrezelTextFieldFeedback.NO_MESSAGE,
    enabled: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var focused by remember { mutableStateOf(false) }

    val state = rememberPrezelTextFieldInteraction(
        value = value,
        enabled = enabled,
        focused = focused,
    ).let { interaction -> PrezelTextFieldState(interaction = interaction, feedback = feedback) }

    PrezelTextField(
        value = value,
        onValueChange = { newValue ->
            val applied = applyTextFieldPolicy(newValue, maxLength)
            if (applied != value) onValueChange(applied)
        },
        placeholder = if (focused) "" else placeholder,
        state = state,
        onFocusChange = { isFocused -> focused = isFocused },
        modifier = modifier,
        label = label,
        trailingIcon = trailingIcon,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

private fun applyTextFieldPolicy(
    value: String,
    maxLength: Int,
): String {
    require(maxLength >= 0) { "maxLength must be >= 0" }
    return value
        .replace("\n", "")
        .take(maxLength)
}

@Composable
private fun PrezelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    state: PrezelTextFieldState,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        label?.let {
            PrezelTextFieldLabel(label = it)
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onFocusChanged { focusState -> onFocusChange(focusState.isFocused) },
            singleLine = true,
            textStyle = PrezelTheme.typography.body2Regular.copy(color = state.textColor()),
            cursorBrush = SolidColor(PrezelTheme.colors.interactiveRegular),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                PrezelTextFieldDecorationBox(
                    innerTextField = innerTextField,
                    showPlaceholder = value.isEmpty(),
                    placeholder = placeholder,
                    trailingIcon = trailingIcon,
                    state = state,
                )
            },
        )

        if (state.supportingText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
            PrezelTextFieldSupportingText(state = state)
        }
    }
}

@Composable
private fun PrezelTextFieldDecorationBox(
    showPlaceholder: Boolean,
    innerTextField: @Composable () -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)?,
    state: PrezelTextFieldState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = state.containerColor(),
        shape = PrezelTheme.shapes.V8,
        border = state.borderStroke(),
        contentColor = state.textColor(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PrezelTheme.spacing.V12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                innerTextField()
                if (showPlaceholder) PrezelTextFieldPlaceholder(placeholder = placeholder)
            }

            trailingIcon?.let { content ->
                Spacer(modifier = Modifier.width(PrezelTheme.spacing.V8))
                CompositionLocalProvider(
                    LocalContentColor provides state.trailingIconColor(),
                    content = content,
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelTextFieldPreview() {
    PreviewSurface {
        PreviewColumn(
            scrollable = true,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PreviewTextFieldItem(
                label = "Interaction - Default / Feedback - Default",
                value = "",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.DEFAULT,
                    feedback = PrezelTextFieldFeedback.Default("헬퍼 메시지"),
                ),
            )

            PreviewTextFieldItem(
                label = "Interaction - Disabled / Feedback - Default",
                value = "",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.DISABLED,
                    feedback = PrezelTextFieldFeedback.Default("헬퍼 메시지"),
                ),
            )
            PreviewTextFieldItem(
                label = "Interaction - Typing / Feedback - Default",
                value = "typing...",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.TYPING,
                    feedback = PrezelTextFieldFeedback.Default("헬퍼 메시지"),
                ),
            )
            PreviewTextFieldItem(
                label = "Interaction - Typed / Feedback - Default",
                value = "typed",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.TYPED,
                    feedback = PrezelTextFieldFeedback.Default("헬퍼 메시지"),
                ),
            )
            PreviewTextFieldItem(
                label = "Interaction - Typed / Feedback - Good",
                value = "typed",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.TYPED,
                    feedback = PrezelTextFieldFeedback.Good("헬퍼 메시지"),
                ),
            )

            PreviewTextFieldItem(
                label = "Interaction - Typed / Feedback - Bad",
                value = "typed",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.TYPED,
                    feedback = PrezelTextFieldFeedback.Bad("헬퍼 메시지"),
                ),
            )
        }
    }
}

@BasicPreview
@Composable
private fun MainPrezelTextFieldPreview() {
    var value by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    PreviewSurface {
        PreviewColumn(scrollable = true) {
            PrezelTextField(
                value = value,
                onValueChange = { newValue -> value = newValue },
                placeholder = "플레이스홀더",
                label = "레이블",
                trailingIcon = {
                    Icon(
                        painter = painterResource(PrezelIcons.Cancel),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(PrezelTheme.shapes.V6)
                            .clickable(
                                indication = ripple(),
                                interactionSource = null,
                                onClick = { value = "" },
                            ),
                    )
                },
                feedback = PrezelTextFieldFeedback.Default("헬퍼 메시지"),
            )

            Spacer(modifier = Modifier.height(20.dp))
            PrezelButton(
                text = "포커스 제거",
                onClick = { focusManager.clearFocus() },
            )
        }
    }
}

@Composable
private fun PreviewTextFieldItem(
    label: String,
    value: String,
    state: PrezelTextFieldState,
    modifier: Modifier = Modifier,
) {
    PrezelTextField(
        value = value,
        onValueChange = {},
        placeholder = "Placeholder",
        label = label,
        state = state,
        modifier = modifier,
        onFocusChange = {},
        trailingIcon = {
            Icon(
                painter = painterResource(PrezelIcons.Blank),
                contentDescription = null,
            )
        },
    )

    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
}
