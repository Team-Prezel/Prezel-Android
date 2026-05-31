package com.team.prezel.feature.analysis.impl.cache

import android.content.Context
import android.provider.OpenableColumns
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * Android ContentResolver를 사용해 content Uri의 원본 파일을 앱 cacheDir에 임시 파일로 복사한다.
 *
 * 원본 파일명을 조회할 수 있으면 확장자를 유지하고, 조회할 수 없는 경우 Uri path 또는 기본 확장자를 사용한다.
 */
internal class AnalysisFileCacheImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : AnalysisFileCache {
    override fun copyUriToCache(
        uriString: String,
        prefix: String,
    ): File {
        val uri = uriString.toUri()
        val displayName = context.contentResolver
            .query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1 && cursor.moveToFirst()) {
                    cursor.getString(displayNameIndex)
                } else {
                    null
                }
            }
        val extension = displayName
            ?.substringAfterLast('.', missingDelimiterValue = "")
            ?.takeIf(String::isNotBlank)
            ?: uri.lastPathSegment
                ?.substringAfterLast('.', missingDelimiterValue = "")
                ?.takeIf(String::isNotBlank)
            ?: DEFAULT_EXTENSION
        val target = File.createTempFile(prefix, ".$extension", context.cacheDir)
        return runCatching {
            context.contentResolver.openInputStream(uri).use { input ->
                requireNotNull(input) { "Cannot open uri: $uriString" }
                target.outputStream().use { output -> input.copyTo(output) }
            }
            target
        }.getOrElse { throwable ->
            target.delete()
            throw throwable
        }
    }

    override fun readTextFromUri(uriString: String): String {
        val uri = uriString.toUri()
        return context.contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "Cannot open uri: $uriString" }
            input.bufferedReader(Charsets.UTF_8).use { reader -> reader.readText() }
        }
    }

    private companion object {
        const val DEFAULT_EXTENSION = "tmp"
    }
}
