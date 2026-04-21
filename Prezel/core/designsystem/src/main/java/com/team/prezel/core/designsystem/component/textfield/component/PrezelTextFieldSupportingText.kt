package com.team.prezel.core.designsystem.component.textfield.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldState
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldStatus
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldStyle
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PrezelTextFieldSupportingText(
    state: PrezelTextFieldStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = state.supportingText,
        style = PrezelTheme.typography.body3Regular,
        color = state.supportingTextColor(),
        modifier = modifier,
        maxLines = 1,
    )
}

@BasicPreview
@Composable
private fun PrezelTextFieldSupportingTextPreview() {
    PreviewSection(title = "Supporting Text") {
        PrezelTextFieldSupportingText(
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Default("헬퍼 메시지"),
            ),
        )

        PrezelTextFieldSupportingText(
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Bad("헬퍼 메시지"),
            ),
        )

        PrezelTextFieldSupportingText(
            state = PrezelTextFieldStyle(
                state = PrezelTextFieldState.TYPED,
                status = PrezelTextFieldStatus.Good("헬퍼 메시지"),
            ),
        )
    }
}
