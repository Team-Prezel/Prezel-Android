package com.team.prezel.core.designsystem.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
annotation class BasicPreview

@Composable
internal fun PreviewScreen(
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    content: @Composable () -> Unit,
) {
    Scaffold(
        containerColor = PrezelTheme.colors.bgRegular,
        contentColor = PrezelTheme.colors.textLarge,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = verticalArrangement,
        ) {
            content()
        }
    }
}

@Composable
internal fun PreviewTitle(title: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = PrezelTheme.typography.title2Bold, color = PrezelTheme.colors.textLarge)
        HorizontalDivider(color = PrezelTheme.colors.borderRegular)
    }
}

@Composable
internal fun PreviewTokenItems(
    items: ImmutableList<Pair<String, Dp>>,
    preview: @Composable (Dp) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { (name, value) ->
            PreviewTokenItem(
                name = name,
                valueLabel = "${value.value}dp",
                preview = { preview(value) },
            )
        }
    }
}

@Composable
internal fun PreviewTokenItem(
    name: String,
    valueLabel: String,
    preview: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = name,
            style = PrezelTheme.typography.body3Medium,
            color = PrezelTheme.colors.textMedium,
            modifier = Modifier.width(120.dp),
        )
        Text(
            text = valueLabel,
            style = PrezelTheme.typography.body3Regular,
            color = PrezelTheme.colors.textSmall,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(4.dp),
        ) {
            preview()
        }
    }
}
