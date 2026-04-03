package com.team.prezel.core.designsystem.component.modal.dialog

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme

@LayoutScopeMarker
class PrezelDialogScope {
    enum class ActionType {
        DEFAULT,
        GOOD,
        BAD,
    }

    @Composable
    fun Action(
        label: String,
        type: ActionType = ActionType.DEFAULT,
        onClick: () -> Unit,
    ) {
        val labelColor = labelColor(type = type)

        PrezelTouchArea(
            onClick = onClick,
            shape = PrezelTheme.shapes.V4,
            modifier = Modifier.semantics { role = Role.Button },
        ) {
            Text(
                text = label,
                style = PrezelTheme.typography.body2Medium,
                color = labelColor,
                modifier = Modifier.padding(vertical = PrezelTheme.spacing.V8),
            )
        }
    }

    @Composable
    private fun labelColor(type: ActionType) =
        when (type) {
            ActionType.DEFAULT -> PrezelTheme.colors.textRegular
            ActionType.GOOD -> PrezelTheme.colors.feedbackGoodRegular
            ActionType.BAD -> PrezelTheme.colors.feedbackBadRegular
        }
}

@BasicPreview
@Composable
private fun PrezelDialogActionPreview() {
    PrezelTheme {
        PreviewSection(
            title = "Dialog Action",
            description = "Dialog에 사용되는 리소스입니다.",
        ) {
            with(PrezelDialogScope()) {
                PreviewValueRow(name = "Default Action") {
                    Action(label = "Action", type = PrezelDialogScope.ActionType.DEFAULT) {}
                }
                PreviewValueRow(name = "Good Action") {
                    Action(label = "Action", type = PrezelDialogScope.ActionType.GOOD) {}
                }
                PreviewValueRow(name = "Bad Action") {
                    Action(label = "Action", type = PrezelDialogScope.ActionType.BAD) {}
                }
            }
        }
    }
}
