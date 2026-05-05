package com.team.prezel.feature.analysis.impl.component

import android.content.Context
import android.provider.OpenableColumns
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun OutlineActionButton(
    text: String,
    modifier: Modifier = Modifier,
    iconResId: Int? = null,
    onClick: () -> Unit,
) {
    PrezelTouchArea(
        onClick = onClick,
        shape = PrezelTheme.shapes.V8,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .clip(PrezelTheme.shapes.V8)
                .border(PrezelTheme.stroke.V1, PrezelTheme.colors.interactiveRegular, PrezelTheme.shapes.V8)
                .padding(horizontal = PrezelTheme.spacing.V16, vertical = PrezelTheme.spacing.V12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (iconResId != null) {
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    tint = PrezelTheme.colors.interactiveRegular,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(PrezelTheme.spacing.V6))
            }
            Text(
                text = text,
                color = PrezelTheme.colors.interactiveRegular,
                style = PrezelTheme.typography.body2Medium,
            )
        }
    }
}

internal fun String.toFileName(context: Context): String {
    val uri = toUri()
    val displayName = runCatching {
        context.contentResolver
            .query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                if (displayNameIndex >= 0 && cursor.moveToFirst()) {
                    cursor.getString(displayNameIndex)
                } else {
                    null
                }
            }
    }.getOrNull()

    return displayName
        ?.takeIf { it.isNotBlank() }
        ?: uri.lastPathSegment.orEmpty().ifBlank { this }
}
