package com.team.prezel.core.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlin.math.max

internal class MediaRecorderSession @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : AudioRecorderSession {
    private var recorder: MediaRecorder? = null
    private var currentAudioFile: File? = null

    override fun start(): Result<Unit> =
        runCatching {
            reset()

            val file = File.createTempFile("recording_", ".m4a", context.cacheDir)
            var pendingRecorder: MediaRecorder? = null
            val newRecorder = runCatching {
                createMediaRecorder(context = context).also { pendingRecorder = it }.apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(file.absolutePath)
                    prepare()
                    start()
                }
            }.getOrElse { throwable ->
                pendingRecorder?.release()
                file.delete()
                throw throwable
            }

            recorder = newRecorder
            currentAudioFile = file
        }.onFailure {
            reset()
        }

    override fun pause(): Result<Unit> =
        runCatching {
            recorder!!.pause()
        }

    override fun resume(): Result<Unit> =
        runCatching {
            recorder!!.resume()
        }

    override fun stop(elapsedSeconds: Int): Result<RecordedAudio> =
        runCatching {
            val file = currentAudioFile!!
            recorder!!.stop()
            RecordedAudio(
                source = AudioSource.RecordedFile(filePath = file.absolutePath),
                durationSeconds = max(elapsedSeconds, 0),
            )
        }.onSuccess {
            releaseRecorder()
        }.onFailure {
            reset()
        }

    override fun reset() {
        releaseRecorder()
        currentAudioFile?.delete()
        currentAudioFile = null
    }

    private fun releaseRecorder() {
        recorder?.release()
        recorder = null
    }
}

internal data class RecordedAudio(
    val source: AudioSource.RecordedFile,
    val durationSeconds: Int,
)

@Suppress("DEPRECATION")
private fun createMediaRecorder(context: Context): MediaRecorder =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        MediaRecorder(context)
    } else {
        MediaRecorder()
    }
