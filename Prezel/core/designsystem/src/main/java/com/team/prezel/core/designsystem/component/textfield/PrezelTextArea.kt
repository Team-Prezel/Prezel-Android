package com.team.prezel.core.designsystem.component.textfield

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
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
import kotlin.math.min

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
    scrollState: ScrollState = rememberScrollState(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    require(maxLength >= 0) { "maxLength는 0 이상이어야 합니다." }

    var focused by remember { mutableStateOf(false) }
    val textFieldState = remember { TextFieldState(initialText = value) }
    val currentValue by rememberUpdatedState(value)
    val currentOnValueChange by rememberUpdatedState(onValueChange)

    LaunchedEffect(value) {
        if (value != textFieldState.text.toString()) {
            textFieldState.setTextAndPlaceCursorAtEnd(value)
        }
    }

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .collect { newValue ->
                if (newValue != currentValue) currentOnValueChange(newValue)
            }
    }

    val style = rememberPrezelTextFieldState(
        value = textFieldState.text.toString(),
        enabled = enabled,
        focused = focused,
    ).let { state -> PrezelTextFieldStyle(state = state, status = status) }

    PrezelTextArea(
        textFieldState = textFieldState,
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
        scrollState = scrollState,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
private fun PrezelTextArea(
    textFieldState: TextFieldState,
    placeholder: String,
    maxLength: Int,
    style: PrezelTextFieldStyle,
    focused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    label: String?,
    enabled: Boolean,
    showCount: Boolean,
    modifier: Modifier = Modifier,
    minHeight: Dp = 72.dp,
    fillContainerHeight: Boolean,
    scrollState: ScrollState,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
) {
    val keyboardActionHandler = remember(keyboardActions, keyboardOptions.imeAction) {
        keyboardActions.toKeyboardActionHandler(keyboardOptions.imeAction)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        label?.let {
            PrezelTextFieldLabel(label = it)
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
        }

        BasicTextField(
            state = textFieldState,
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
            onKeyboardAction = keyboardActionHandler,
            inputTransformation = InputTransformation.maxLength(maxLength),
            scrollState = scrollState,
            decorator = { innerTextField ->
                PrezelTextAreaDecorationBox(
                    innerTextField = innerTextField,
                    showPlaceholder = !focused && textFieldState.text.isEmpty(),
                    placeholder = placeholder,
                    state = style,
                    showCounter = showCount,
                    scrollState = scrollState,
                    counter = {
                        if (showCount) {
                            Counter(currentLength = textFieldState.text.length, maxLength = maxLength, state = style)
                        }
                    },
                    modifier = when {
                        fillContainerHeight -> Modifier.fillMaxHeight()
                        else -> Modifier.heightIn(min = minHeight)
                    },
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
    scrollState: ScrollState,
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
        val showScrollbar = scrollState.maxValue > 0
        val endPadding = if (showScrollbar) PrezelTheme.spacing.V8 else PrezelTheme.spacing.V12
        val scrollbarContentPadding = if (showScrollbar) PrezelTheme.spacing.V8 else 0.dp

        Box(
            modifier = Modifier
                .then(if (fillContainerHeight) Modifier.fillMaxSize() else Modifier.fillMaxWidth())
                .padding(
                    start = PrezelTheme.spacing.V12,
                    top = PrezelTheme.spacing.V12,
                    end = endPadding,
                    bottom = PrezelTheme.spacing.V12,
                ),
        ) {
            Box(
                modifier = Modifier
                    .then(if (fillContainerHeight) Modifier.fillMaxSize() else Modifier.fillMaxWidth())
                    .padding(
                        end = scrollbarContentPadding,
                        bottom = if (showCounter) PrezelTheme.spacing.V24 else 0.dp,
                    ),
            ) {
                innerTextField()
                if (showPlaceholder) {
                    PrezelTextFieldPlaceholder(placeholder = placeholder)
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = scrollbarContentPadding),
            ) {
                counter()
            }

            if (showScrollbar) {
                PrezelTextAreaScrollbar(
                    scrollState = scrollState,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .width(PrezelTheme.spacing.V4),
                )
            }
        }
    }
}

@Composable
private fun PrezelTextAreaScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    val color = PrezelTheme.colors.borderLarge
    val minThumbHeight = PrezelTheme.spacing.V24

    Canvas(modifier = modifier) {
        if (scrollState.maxValue <= 0 || scrollState.viewportSize <= 0) return@Canvas

        val viewportHeight = scrollState.viewportSize.toFloat()
        val contentHeight = viewportHeight + scrollState.maxValue
        val minThumbHeightPx = min(minThumbHeight.toPx(), size.height)
        val thumbHeight = (size.height * viewportHeight / contentHeight)
            .coerceIn(minThumbHeightPx, size.height)
        val thumbOffset = (size.height - thumbHeight) * scrollState.value / scrollState.maxValue

        drawRoundRect(
            color = color,
            topLeft = Offset(x = 0f, y = thumbOffset),
            size = Size(width = size.width, height = thumbHeight),
            cornerRadius = CornerRadius(size.width / 2f),
        )
    }
}

private fun KeyboardActions.toKeyboardActionHandler(imeAction: ImeAction): KeyboardActionHandler? {
    val action = when (imeAction) {
        ImeAction.Done -> onDone
        ImeAction.Go -> onGo
        ImeAction.Next -> onNext
        ImeAction.Previous -> onPrevious
        ImeAction.Search -> onSearch
        ImeAction.Send -> onSend
        else -> null
    } ?: return null

    return KeyboardActionHandler { performDefaultAction ->
        action.invoke(
            object : KeyboardActionScope {
                override fun defaultKeyboardAction(imeAction: ImeAction) = performDefaultAction()
            },
        )
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

@BasicPreview
@Composable
private fun PrezelTextAreaScrollbarPreview() {
    PreviewTextAreaState(title = "Type - With Scrollbar") {
        PrezelTextAreaPreviewItem(
            label = "Label",
            value = "Lorem ipsum dolor sit amet consectetur. Consequat quis viverra nulla in aliquam sed " +
                "scelerisque odio gravida. At urna congue vulputate facilisis id et viverra pellentesque " +
                "tempus. Blandit et faucibus iaculis dictum pharetra. Magna elit lacus nullam facilisi amet " +
                "urna pulvinar.",
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Default("Helper"),
            ),
            modifier = Modifier.height(200.dp),
            showCount = false,
            fillContainerHeight = true,
            maxLength = 500,
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
    showCount: Boolean = true,
    fillContainerHeight: Boolean = false,
    maxLength: Int = 100,
) {
    val textFieldState = remember { TextFieldState(initialText = value) }

    PrezelTextArea(
        textFieldState = textFieldState,
        placeholder = "Placeholder",
        label = label,
        style = state,
        maxLength = maxLength,
        focused = focused,
        modifier = modifier,
        onFocusChange = {},
        enabled = enabled,
        showCount = showCount,
        minHeight = 72.dp,
        fillContainerHeight = fillContainerHeight,
        scrollState = rememberScrollState(),
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
    )

    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
}
