package com.team.prezel.core.designsystem.component.textfield.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldFeedback
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldInteraction
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldState
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PrezelTextFieldSupportingText(
    state: PrezelTextFieldState,
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
            state = PrezelTextFieldState(
                interaction = PrezelTextFieldInteraction.TYPED,
                feedback = PrezelTextFieldFeedback.Default("헬퍼 메시지"),
            ),
        )

        PrezelTextFieldSupportingText(
            state = PrezelTextFieldState(
                interaction = PrezelTextFieldInteraction.TYPED,
                feedback = PrezelTextFieldFeedback.Bad("헬퍼 메시지"),
            ),
        )

        PrezelTextFieldSupportingText(
            state = PrezelTextFieldState(
                interaction = PrezelTextFieldInteraction.TYPED,
                feedback = PrezelTextFieldFeedback.Good("헬퍼 메시지"),
            ),
        )
    }
}
