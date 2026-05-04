package com.team.prezel.core.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlin.math.max

internal class MediaRecordingAudioController @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : RecordingAudioController {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var recordingStartedAt: Long = 0L
    private var recordingFile: File? = null

    override fun startRecording(): Result<String> =
        runCatching {
            stopPlayback()
            releaseRecorder()
            val previousRecordingFile = recordingFile

            val file = File.createTempFile("recording_", ".m4a", context.cacheDir)
            var pendingRecorder: MediaRecorder? = null
            val newRecorder = runCatching {
                val recorder = createMediaRecorder()
                pendingRecorder = recorder
                recorder.apply {
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
            recordingFile = file
            recordingStartedAt = System.currentTimeMillis()
            previousRecordingFile?.delete()
            file.absolutePath
        }

    override fun stopRecording(): Result<Int> {
        if (recorder == null || recordingStartedAt <= 0L) {
            return Result.failure(IllegalStateException("Recording is not active."))
        }

        val durationSeconds = ((System.currentTimeMillis() - recordingStartedAt) / 1_000L).toInt()

        return runCatching {
            recorder?.stop()
            max(durationSeconds, 0)
        }.also {
            releaseRecorder()
        }
    }

    override fun startPlayback(
        filePath: String,
        onComplete: () -> Unit,
    ): Result<Int> =
        runCatching {
            stopPlayback()

            var pendingPlayer: MediaPlayer? = null
            val newPlayer = runCatching {
                val mediaPlayer = MediaPlayer()
                pendingPlayer = mediaPlayer
                mediaPlayer.apply {
                    setDataSource(filePath)
                    prepare()
                    setOnCompletionListener {
                        stopPlayback()
                        onComplete()
                    }
                    start()
                }
            }.getOrElse { throwable ->
                pendingPlayer?.release()
                throw throwable
            }

            player = newPlayer
            newPlayer.duration.toSeconds()
        }

    override fun stopPlayback() {
        player?.runCatching { stop() }
        player?.release()
        player = null
    }

    override fun playbackPositionSeconds(): Int = runCatching { player?.currentPosition?.toSeconds() }.getOrNull() ?: 0

    override fun release() {
        releaseRecorder()
        stopPlayback()
        recordingFile?.delete()
        recordingFile = null
    }

    @Suppress("DEPRECATION")
    private fun createMediaRecorder(): MediaRecorder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }

    private fun releaseRecorder() {
        recorder?.release()
        recorder = null
        recordingStartedAt = 0L
    }
}

private fun Int.toSeconds(): Int = this / 1_000
