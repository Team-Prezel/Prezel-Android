package com.team.prezel.feature.home.impl.practice.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlin.math.max

internal class PracticeRecordingAudioController(
    private val context: Context,
) {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var recordingStartedAt: Long = 0L
    private var recordingFile: File? = null

    fun startRecording(): Result<String> =
        runCatching {
            stopPlayback()
            releaseRecorder()

            val file = File.createTempFile("practice_recording_", ".m4a", context.cacheDir)
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
            file.absolutePath
        }

    fun stopRecording(): Result<Int> {
        val durationSeconds = ((System.currentTimeMillis() - recordingStartedAt) / 1_000L).toInt()

        return runCatching {
            recorder?.stop()
            max(durationSeconds, 0)
        }.also {
            releaseRecorder()
        }
    }

    fun startPlayback(
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

    fun stopPlayback() {
        player?.runCatching { stop() }
        player?.release()
        player = null
    }

    fun playbackPositionSeconds(): Int = runCatching { player?.currentPosition?.toSeconds() }.getOrNull() ?: 0

    fun release() {
        releaseRecorder()
        stopPlayback()
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

internal class PracticeRecordingAudioControllerFactory @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    fun create(): PracticeRecordingAudioController = PracticeRecordingAudioController(context)
}

private fun Int.toSeconds(): Int = this / 1_000
