package com.team.prezel.core.designsystem.component.textfield.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldState
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldStatus
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldStyle
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PrezelTextFieldSupportingText(
    text: String,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = PrezelTheme.typography.body3Regular,
        color = textColor,
        modifier = modifier,
        maxLines = 1,
    )
}

@BasicPreview
@Composable
private fun PrezelTextFieldSupportingTextPreview() {
    PreviewSection(title = "Supporting Text") {
        val default = PrezelTextFieldStyle(
            state = PrezelTextFieldState.TYPED,
            status = PrezelTextFieldStatus.Default("헬퍼 메시지"),
        )
        PrezelTextFieldSupportingText(
            text = default.supportingText,
            textColor = default.supportingTextColor(),
        )

        val bad = PrezelTextFieldStyle(
            state = PrezelTextFieldState.TYPED,
            status = PrezelTextFieldStatus.Bad("헬퍼 메시지"),
        )
        PrezelTextFieldSupportingText(
            text = bad.supportingText,
            textColor = bad.supportingTextColor(),
        )

        val good = PrezelTextFieldStyle(
            state = PrezelTextFieldState.TYPED,
            status = PrezelTextFieldStatus.Good("헬퍼 메시지"),
        )
        PrezelTextFieldSupportingText(
            text = good.supportingText,
            textColor = good.supportingTextColor(),
        )
    }
}
