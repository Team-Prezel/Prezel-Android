package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewColumn
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewSurface
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 라디오 버튼의 크기 정의.
 *
 * @property value 라디오 버튼의 터치 영역 크기
 */
enum class PrezelRadioSize(
    val value: Dp,
) {
    REGULAR(40.dp),
    LARGE(48.dp),
}

/**
 * 아이콘만 표시되는 라디오 버튼.
 *
 * @param checked 현재 선택 여부
 * @param onCheckedChange 선택 상태 변경 콜백
 * @param size 라디오 버튼 크기
 */
@Composable
fun PrezelRadio(
    checked: Boolean,
    onCheckedChange: (checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: PrezelRadioSize = PrezelRadioSize.REGULAR,
) {
    PrezelRadioIcon(
        checked = checked,
        size = size,
        modifier = modifier
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.RadioButton,
                indication = null,
                interactionSource = null,
            ),
    )
}

/**
 * 텍스트와 함께 표시되는 라디오 버튼.
 *
 * @param text 라디오 버튼 라벨 텍스트
 * @param checked 현재 선택 여부
 * @param onCheckedChange 선택 상태 변경 콜백
 * @param size 라디오 버튼 크기
 */
@Composable
fun PrezelRadio(
    text: String,
    checked: Boolean,
    onCheckedChange: (checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: PrezelRadioSize = PrezelRadioSize.REGULAR,
    textStyle: TextStyle = PrezelTheme.typography.body2Medium,
    textColor: Color = PrezelTheme.colors.textLarge,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(PrezelTheme.shapes.V6)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.RadioButton,
                indication = ripple(),
                interactionSource = null,
            ),
    ) {
        PrezelRadioIcon(checked = checked, size = size)
        Spacer(modifier = Modifier.width(PrezelTheme.spacing.V4))
        Text(text = text, style = textStyle, color = textColor)
    }
}

@Composable
private fun PrezelRadioIcon(
    checked: Boolean,
    modifier: Modifier = Modifier,
    size: PrezelRadioSize = PrezelRadioSize.REGULAR,
) {
    val tintColor = if (checked) PrezelTheme.colors.interactiveRegular else PrezelTheme.colors.iconDisabled
    val icon = if (checked) PrezelIcons.RadioCircleFilled else PrezelIcons.RadioCircleOutlined

    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        tint = tintColor,
        modifier = Modifier
            .size(size.value)
            .then(modifier)
            .padding(PrezelTheme.spacing.V8),
    )
}

@BasicPreview
@Composable
private fun PrezelRadioPreview() {
    var checked by remember { mutableStateOf(false) }

    PreviewSurface {
        PreviewColumn(scrollable = true) {
            PreviewSection(
                title = "PrezelRadioSize.REGULAR",
                showDivider = true,
            ) {
                Text("Checked: true")
                PrezelRadio(checked = true, onCheckedChange = {}, size = PrezelRadioSize.REGULAR)
                Text("Checked: false")
                PrezelRadio(checked = false, onCheckedChange = {}, size = PrezelRadioSize.REGULAR)
                Text("PrezelRadio with text")
                PrezelRadio(checked = checked, onCheckedChange = { checked = it }, text = "텍스트", size = PrezelRadioSize.REGULAR)
            }

            PreviewSection(
                title = "PrezelRadioSize.LARGE",
                showDivider = true,
            ) {
                Text("Checked: true")
                PrezelRadio(checked = true, onCheckedChange = {}, size = PrezelRadioSize.LARGE)
                Text("Checked: false")
                PrezelRadio(checked = false, onCheckedChange = {}, size = PrezelRadioSize.LARGE)
                Text("PrezelRadio with text")
                PrezelRadio(checked = checked, onCheckedChange = { checked = it }, text = "텍스트", size = PrezelRadioSize.LARGE)
            }
        }
    }
}
