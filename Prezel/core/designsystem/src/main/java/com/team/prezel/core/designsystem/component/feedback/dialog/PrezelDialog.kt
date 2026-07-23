package com.team.prezel.core.designsystem.component.feedback.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelDialog(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    content: @Composable PrezelDialogScope.() -> Unit,
) {
    val scope = remember { PrezelDialogScope() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = PrezelTheme.spacing.V20)
                .clip(shape = PrezelTheme.shapes.V12)
                .background(color = PrezelTheme.colors.bgRegular)
                .padding(horizontal = PrezelTheme.spacing.V24),
        ) {
            DialogContent(
                title = title,
                description = description,
            )

            ActionSection { scope.content() }
        }
    }
}

@Composable
private fun DialogContent(
    title: String,
    description: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = PrezelTheme.spacing.V24),
    ) {
        Text(text = title, style = PrezelTheme.typography.title2Bold, color = PrezelTheme.colors.textLarge)
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
        description?.let { text ->
            Text(text = text, style = PrezelTheme.typography.body3Medium, color = PrezelTheme.colors.textRegular)
        }
    }
}

@Composable
private fun ActionSection(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = PrezelTheme.spacing.V20),
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V24),
        ) {
            content()
        }
    }
}

@BasicPreview
@Composable
private fun PrezelDialogPreview() {
    PrezelTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PrezelDialog(
                title = "Title",
                description = "Description",
                onDismiss = {},
            ) {
                Action(label = "취소") {}
                Action(label = "탈퇴", type = PrezelDialogScope.ActionType.BAD) {}
            }
        }
    }
}
