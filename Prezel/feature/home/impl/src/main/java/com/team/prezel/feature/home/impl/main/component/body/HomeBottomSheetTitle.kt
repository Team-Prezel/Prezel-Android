package com.team.prezel.feature.home.impl.main.component.body

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun HomeBottomSheetTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        color = PrezelTheme.colors.textLarge,
        style = PrezelTheme.typography.body2Bold,
    )
}

@BasicPreview
@Composable
private fun HomeBottomSheetTitlePreview() {
    PrezelTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            HomeBottomSheetTitle(title = "프레젤 홈 바텀 시트 타이틀")
        }
    }
}
