package com.team.prezel.feature.analysis.impl.component

import android.content.Context
import android.provider.OpenableColumns
import androidx.core.net.toUri

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
