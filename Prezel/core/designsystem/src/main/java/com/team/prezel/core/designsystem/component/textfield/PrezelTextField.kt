package com.team.prezel.core.designsystem.component.textfield

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
    status: PrezelTextFieldStatus = PrezelTextFieldStatus.DEFAULT,
    enabled: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var focused by remember { mutableStateOf(false) }

    val state = rememberPrezelTextFieldState(
        value = value,
        enabled = enabled,
        focused = focused,
    ).let { state -> PrezelTextFieldStyle(state = state, status = status) }

    PrezelTextField(
        value = value,
        onValueChange = { newValue ->
            val applied = applyPrezelTextInputPolicy(newValue, maxLength)
            if (applied != value) onValueChange(applied)
        },
        placeholder = placeholder,
        state = state,
        focused = focused,
        onFocusChange = { isFocused -> focused = isFocused },
        modifier = modifier,
        label = label,
        trailingIcon = trailingIcon,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
private fun PrezelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    state: PrezelTextFieldStyle,
    focused: Boolean,
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
                    showPlaceholder = !focused && value.isEmpty(),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PrezelTheme.spacing.V12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                innerTextField()
                if (showPlaceholder) {
                    PrezelTextFieldPlaceholder(placeholder = placeholder)
                }
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
private fun PrezelTextFieldDefaultStatePreview() {
    PreviewTextFieldState(title = "State - Default") {
        PreviewTextFieldItem(
            label = "Status - Default",
            value = "",
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.DEFAULT,
                status = PrezelTextFieldStatus.Default("헬퍼 메시지"),
            ),
        )
    }
}

@BasicPreview
@Composable
private fun PrezelTextFieldDisabledStatePreview() {
    PreviewTextFieldState(title = "State - Disabled") {
        PreviewTextFieldItem(
            label = "Status - Default",
            value = "",
            enabled = false,
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.DISABLED,
                status = PrezelTextFieldStatus.Default("헬퍼 메시지"),
            ),
        )
    }
}

@BasicPreview
@Composable
private fun PrezelTextFieldTypingStatePreview() {
    PreviewTextFieldState(title = "State - Typing") {
        PreviewTextFieldItem(
            label = "Status - Default",
            value = "",
            focused = true,
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPING,
                status = PrezelTextFieldStatus.Default("헬퍼 메시지"),
            ),
        )
        PreviewTextFieldItem(
            label = "Status - Good",
            value = "",
            focused = true,
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPING,
                status = PrezelTextFieldStatus.Good("헬퍼 메시지"),
            ),
        )
        PreviewTextFieldItem(
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
private fun PrezelTextFieldTypedStatePreview() {
    PreviewTextFieldState(title = "State - Typed") {
        PreviewTextFieldItem(
            label = "Status - Default",
            value = "typed",
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Default("헬퍼 메시지"),
            ),
        )
        PreviewTextFieldItem(
            label = "Status - Good",
            value = "typed",
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Good("헬퍼 메시지"),
            ),
        )
        PreviewTextFieldItem(
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
private fun PreviewTextFieldState(
    title: String,
    content: @Composable () -> Unit,
) {
    PreviewSurface {
        PreviewColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
private fun PreviewTextFieldItem(
    label: String,
    value: String,
    state: PrezelTextFieldStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    focused: Boolean = false,
) {
    PrezelTextField(
        value = value,
        onValueChange = {},
        placeholder = "Placeholder",
        label = label,
        state = state,
        focused = focused,
        modifier = modifier,
        onFocusChange = {},
        enabled = enabled,
        trailingIcon = {
            Icon(
                painter = painterResource(PrezelIcons.Blank),
                contentDescription = null,
            )
        },
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
    )

    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
}
