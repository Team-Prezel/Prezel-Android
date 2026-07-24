package com.team.prezel.core.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
    minHeight: Dp = 72.dp,
    fillContainerHeight: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var focused by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length))) }

    LaunchedEffect(value) {
        if (value != textFieldValue.text) {
            textFieldValue = TextFieldValue(text = value, selection = TextRange(value.length))
        }
    }

    val style = rememberPrezelTextFieldState(
        value = textFieldValue.text,
        enabled = enabled,
        focused = focused,
    ).let { state -> PrezelTextFieldStyle(state = state, status = status) }

    PrezelTextArea(
        value = textFieldValue,
        onValueChange = { newValue ->
            val applied = applyPrezelTextInputPolicy(currentValue = textFieldValue, newValue = newValue, maxLength = maxLength)
            if (applied != textFieldValue) {
                textFieldValue = applied
                if (applied.text != value) onValueChange(applied.text)
            }
        },
        placeholder = placeholder,
        style = style,
        maxLength = maxLength,
        focused = focused,
        onFocusChange = { isFocused -> focused = isFocused },
        modifier = modifier,
        label = label,
        enabled = enabled,
        showCount = showCount,
        minHeight = minHeight,
        fillContainerHeight = fillContainerHeight,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
private fun PrezelTextArea(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    maxLength: Int,
    style: PrezelTextFieldStyle,
    focused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    label: String?,
    enabled: Boolean,
    showCount: Boolean,
    minHeight: Dp = 72.dp,
    fillContainerHeight: Boolean,
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
                .then(
                    if (fillContainerHeight) {
                        Modifier.weight(1f)
                    } else {
                        Modifier.heightIn(min = minHeight)
                    },
                ).onFocusChanged { focusState -> onFocusChange(focusState.isFocused) },
            textStyle = PrezelTheme.typography.body2Regular.copy(color = style.textColor()),
            cursorBrush = SolidColor(PrezelTheme.colors.interactiveRegular),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                PrezelTextAreaDecorationBox(
                    innerTextField = innerTextField,
                    showPlaceholder = !focused && value.text.isEmpty(),
                    placeholder = placeholder,
                    state = style,
                    showCounter = showCount,
                    counter = {
                        if (showCount) {
                            Counter(currentLength = value.text.length, maxLength = maxLength, state = style)
                        }
                    },
                    modifier = if (fillContainerHeight) Modifier.fillMaxHeight() else Modifier.heightIn(min = minHeight),
                    fillContainerHeight = fillContainerHeight,
                )
            },
        )

        if (style.supportingText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
            PrezelTextFieldSupportingText(text = style.supportingText, textColor = style.supportingTextColor())
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
    showCounter: Boolean,
    modifier: Modifier = Modifier,
    fillContainerHeight: Boolean = false,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = state.containerColor(),
        shape = PrezelTheme.shapes.V8,
        border = state.borderStroke(),
        contentColor = state.textColor(),
    ) {
        Box(
            modifier = Modifier
                .then(if (fillContainerHeight) Modifier.fillMaxSize() else Modifier.fillMaxWidth())
                .padding(PrezelTheme.spacing.V12),
        ) {
            Box(
                modifier = Modifier
                    .then(if (fillContainerHeight) Modifier.fillMaxSize() else Modifier.fillMaxWidth())
                    .padding(bottom = if (showCounter) PrezelTheme.spacing.V24 else 0.dp),
            ) {
                innerTextField()
                if (showPlaceholder) {
                    PrezelTextFieldPlaceholder(placeholder = placeholder)
                }
            }

            Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                counter()
            }
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
        value = TextFieldValue(text = value, selection = TextRange(value.length)),
        onValueChange = {},
        placeholder = "Placeholder",
        label = label,
        style = state,
        maxLength = 100,
        focused = focused,
        modifier = modifier,
        onFocusChange = {},
        enabled = enabled,
        showCount = true,
        fillContainerHeight = false,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
    )

    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
}
