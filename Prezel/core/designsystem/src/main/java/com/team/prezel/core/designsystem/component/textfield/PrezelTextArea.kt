package com.team.prezel.core.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldLabel
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldPlaceholder
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldSupportingText
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    label: String? = null,
    feedback: PrezelTextFieldFeedback = PrezelTextFieldFeedback.NO_MESSAGE,
    enabled: Boolean = true,
    showCount: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var focused by remember { mutableStateOf(false) }

    val state = rememberPrezelTextFieldInteraction(
        value = value,
        enabled = enabled,
        focused = focused,
    ).let { interaction -> PrezelTextFieldState(interaction = interaction, feedback = feedback) }

    PrezelTextArea(
        value = value,
        onValueChange = { newValue ->
            val applied = applyTextAreaPolicy(newValue, maxLength)
            if (applied != value) onValueChange(applied)
        },
        placeholder = if (focused) "" else placeholder,
        state = state,
        maxLength = maxLength,
        onFocusChange = { isFocused -> focused = isFocused },
        modifier = modifier,
        label = label,
        enabled = enabled,
        showCount = showCount,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

private fun applyTextAreaPolicy(
    value: String,
    maxLength: Int,
): String {
    require(maxLength >= 0) { "maxLength must be >= 0" }
    return value
        .replace("\n", "")
        .take(maxLength)
}

@Composable
private fun PrezelTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLength: Int,
    state: PrezelTextFieldState,
    onFocusChange: (Boolean) -> Unit,
    label: String?,
    enabled: Boolean,
    showCount: Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
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
                .heightIn(min = 72.dp)
                .onFocusChanged { focusState -> onFocusChange(focusState.isFocused) },
            textStyle = PrezelTheme.typography.body2Regular.copy(color = state.textColor()),
            cursorBrush = SolidColor(PrezelTheme.colors.interactiveRegular),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                PrezelTextAreaDecorationBox(
                    innerTextField = innerTextField,
                    showPlaceholder = value.isEmpty(),
                    placeholder = placeholder,
                    state = state,
                    counter = {
                        if (showCount) {
                            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
                            Counter(currentLength = value.length, maxLength = maxLength, state = state)
                        }
                    },
                    modifier = Modifier.heightIn(min = 72.dp),
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
private fun Counter(
    currentLength: Int,
    maxLength: Int,
    state: PrezelTextFieldState,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "$currentLength / $maxLength",
        style = PrezelTheme.typography.caption1Medium,
        color = state.textColor(),
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.End,
    )
}

@Composable
private fun PrezelTextAreaDecorationBox(
    showPlaceholder: Boolean,
    innerTextField: @Composable () -> Unit,
    counter: @Composable () -> Unit,
    placeholder: String,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PrezelTheme.spacing.V12),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box {
                innerTextField()
                if (showPlaceholder) PrezelTextFieldPlaceholder(placeholder = placeholder)
            }

            counter()
        }
    }
}

@ThemePreview
@Composable
private fun PrezelTextAreaPrezelPreview() {
    PrezelTheme {
        PreviewScaffold(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            PrezelTextAreaPreviewItem(
                label = "Interaction - Default / Feedback - Default",
                value = "",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.DEFAULT,
                ),
            )

            PrezelTextAreaPreviewItem(
                label = "Interaction - Disabled / Feedback - Default",
                value = "",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.DISABLED,
                ),
            )

            PrezelTextAreaPreviewItem(
                label = "Interaction - Typing / Feedback - Default",
                value = "typing...",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.TYPING,
                ),
            )

            PrezelTextAreaPreviewItem(
                label = "Interaction - Typed / Feedback - Default",
                value = "typed",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.TYPED,
                    feedback = PrezelTextFieldFeedback.Default("헬퍼 메시지"),
                ),
            )
            PrezelTextAreaPreviewItem(
                label = "Interaction - Typed / Feedback - Good",
                value = "typed",
                state = PrezelTextFieldState(
                    interaction = PrezelTextFieldInteraction.TYPED,
                    feedback = PrezelTextFieldFeedback.Good("헬퍼 메시지"),
                ),
            )

            PrezelTextAreaPreviewItem(
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

@ThemePreview
@Composable
private fun MainPrezelTextAreaPreview() {
    var value by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    PrezelTheme {
        PreviewScaffold {
            PrezelTextArea(
                value = value,
                onValueChange = { newValue -> value = newValue },
                maxLength = 100,
                placeholder = "플레이스홀더",
                label = "레이블",
                feedback = PrezelTextFieldFeedback.Default("헬퍼 메시지"),
                showCount = true,
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
private fun PrezelTextAreaPreviewItem(
    label: String,
    value: String,
    state: PrezelTextFieldState,
    modifier: Modifier = Modifier,
) {
    PrezelTextArea(
        value = value,
        onValueChange = {},
        placeholder = "Placeholder",
        label = label,
        state = state,
        maxLength = 100,
        modifier = modifier,
        onFocusChange = {},
        enabled = true,
        showCount = true,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
    )

    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
}
