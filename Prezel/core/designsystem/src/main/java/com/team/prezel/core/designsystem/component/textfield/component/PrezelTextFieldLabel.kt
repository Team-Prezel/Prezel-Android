package com.team.prezel.core.designsystem.component.textfield.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PrezelTextFieldLabel(
    label: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = PrezelTheme.typography.body3Medium,
        color = PrezelTheme.colors.textMedium,
        modifier = modifier,
        maxLines = 1,
    )
}

@BasicPreview
@Composable
private fun PrezelTextFieldLabelPreview() {
    PreviewSection(title = "TextField Label") {
        PrezelTextFieldLabel(label = "Label", modifier = Modifier.padding(8.dp))
    }
}
