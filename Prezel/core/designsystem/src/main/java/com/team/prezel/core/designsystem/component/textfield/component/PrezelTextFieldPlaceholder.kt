package com.team.prezel.core.designsystem.component.textfield.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PrezelTextFieldPlaceholder(
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = placeholder,
        maxLines = 1,
        style = PrezelTheme.typography.body2Regular.copy(
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
        color = PrezelTheme.colors.textSmall,
        modifier = modifier,
    )
}

@BasicPreview
@Composable
private fun PrezelTextFieldPlaceholderPreview() {
    PrezelTheme {
        PrezelTextFieldPlaceholder(
            placeholder = "Placeholder",
            modifier = Modifier.padding(16.dp),
        )
    }
}
