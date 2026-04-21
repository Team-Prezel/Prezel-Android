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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldLabel
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldPlaceholder
import com.team.prezel.core.designsystem.component.textfield.component.PrezelTextFieldSupportingText
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewColumn
import com.team.prezel.core.designsystem.preview.PreviewSurface
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    label: String? = null,
    status: PrezelTextFieldStatus = PrezelTextFieldStatus.DEFAULT,
    enabled: Boolean = true,
    showCount: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var focused by remember { mutableStateOf(false) }

    val state = rememberPrezelTextFieldState(
        value = value,
        enabled = enabled,
        focused = focused,
    ).let { state -> PrezelTextFieldStyle(state = state, status = status) }

    PrezelTextArea(
        value = value,
        onValueChange = { newValue ->
            val applied = applyPrezelTextInputPolicy(newValue, maxLength)
            if (applied != value) onValueChange(applied)
        },
        placeholder = placeholder,
        state = state,
        maxLength = maxLength,
        focused = focused,
        onFocusChange = { isFocused -> focused = isFocused },
        modifier = modifier,
        label = label,
        enabled = enabled,
        showCount = showCount,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
private fun PrezelTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLength: Int,
    state: PrezelTextFieldStyle,
    focused: Boolean,
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
                    showPlaceholder = !focused && value.isEmpty(),
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
    state: PrezelTextFieldStyle,
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
    state: PrezelTextFieldStyle,
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
                if (showPlaceholder) {
                    PrezelTextFieldPlaceholder(placeholder = placeholder)
                }
            }

            counter()
        }
    }
}

@BasicPreview
@Composable
private fun PrezelTextAreaDefaultStatePreview() {
    PreviewTextAreaState(title = "State - Default") {
        PrezelTextAreaPreviewItem(
            label = "Status - Default",
            value = "",
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.DEFAULT,
            ),
        )
    }
}

@BasicPreview
@Composable
private fun PrezelTextAreaDisabledStatePreview() {
    PreviewTextAreaState(title = "State - Disabled") {
        PrezelTextAreaPreviewItem(
            label = "Status - Default",
            value = "",
            enabled = false,
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.DISABLED,
            ),
        )
    }
}

@BasicPreview
@Composable
private fun PrezelTextAreaTypingStatePreview() {
    PreviewTextAreaState(title = "State - Typing") {
        PrezelTextAreaPreviewItem(
            label = "Status - Default",
            value = "",
            focused = true,
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPING,
            ),
        )
        PrezelTextAreaPreviewItem(
            label = "Status - Good",
            value = "",
            focused = true,
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPING,
                status = PrezelTextFieldStatus.Good("헬퍼 메시지"),
            ),
        )
        PrezelTextAreaPreviewItem(
            label = "Status - Bad",
            value = "",
            focused = true,
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPING,
                status = PrezelTextFieldStatus.Bad("헬퍼 메시지"),
            ),
        )
    }
}

@BasicPreview
@Composable
private fun PrezelTextAreaTypedStatePreview() {
    PreviewTextAreaState(title = "State - Typed") {
        PrezelTextAreaPreviewItem(
            label = "Status - Default",
            value = "typed",
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Default("헬퍼 메시지"),
            ),
        )
        PrezelTextAreaPreviewItem(
            label = "Status - Good",
            value = "typed",
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Good("헬퍼 메시지"),
            ),
        )
        PrezelTextAreaPreviewItem(
            label = "Status - Bad",
            value = "typed",
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Bad("헬퍼 메시지"),
            ),
        )
    }
}

@Composable
private fun PreviewTextAreaState(
    title: String,
    content: @Composable () -> Unit,
) {
    PreviewSurface {
        PreviewColumn(
            scrollable = true,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = PrezelTheme.typography.body2Bold,
                color = PrezelTheme.colors.textLarge,
            )
            content()
        }
    }
}

@Composable
private fun PrezelTextAreaPreviewItem(
    label: String,
    value: String,
    state: PrezelTextFieldStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    focused: Boolean = false,
) {
    PrezelTextArea(
        value = value,
        onValueChange = {},
        placeholder = "Placeholder",
        label = label,
        state = state,
        maxLength = 100,
        focused = focused,
        modifier = modifier,
        onFocusChange = {},
        enabled = enabled,
        showCount = true,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
    )

    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
}
