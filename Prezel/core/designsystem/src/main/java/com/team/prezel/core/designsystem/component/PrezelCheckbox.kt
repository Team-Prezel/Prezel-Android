package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder

enum class CheckboxSize {
    REGULAR,
    LARGE,
}

@Composable
fun PrezelCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    size: CheckboxSize = CheckboxSize.REGULAR,
    onCheckedChange: (Boolean) -> Unit,
) {
    val checkboxSize = when (size) {
        CheckboxSize.REGULAR -> 24.dp
        CheckboxSize.LARGE -> 32.dp
    }

    val iconRes =
        if (checked) {
            PrezelIcons.CheckCircleFilled
        } else {
            PrezelIcons.CheckCircleOutlined
        }

    val iconColor =
        if (checked) {
            PrezelTheme.colors.feedbackGoodRegular
        } else {
            PrezelTheme.colors.iconDisabled
        }

    Box(
        modifier = modifier
            .padding(all = PrezelTheme.spacing.V8)
            .toggleable(
                value = checked,
                interactionSource = null,
                indication = null,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = stringResource(R.string.core_designsystem_checkbox_desc),
            modifier = Modifier.size(checkboxSize),
            tint = iconColor,
        )
    }
}

@BasicPreview
@Composable
private fun PrezelCheckboxPreview() {
    var checkState by remember { mutableStateOf(true) }

    PreviewSection(
        title = "Checkbox",
        description = "Checkbox는 목록에서 선택할 항목이 여러 개 있을 때 사용됩니다.",
    ) {
        PreviewValueRow(name = "Regular") {
            PrezelCheckbox(
                checked = checkState,
                modifier = Modifier.drawDashBorder(),
                size = CheckboxSize.REGULAR,
                onCheckedChange = { checkState = it },
            )
        }
        PreviewValueRow(name = "Large") {
            PrezelCheckbox(
                checked = !checkState,
                modifier = Modifier.drawDashBorder(),
                size = CheckboxSize.LARGE,
                onCheckedChange = { checkState = it },
            )
        }
    }
}
