package com.team.prezel.feature.home.impl.practice

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

    fun startRecording(): String {
        stopPlayback()
        releaseRecorder()

        val file = File.createTempFile("practice_recording_", ".m4a", context.cacheDir)
        val newRecorder = createMediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }

        recorder = newRecorder
        recordingFile = file
        recordingStartedAt = System.currentTimeMillis()
        return file.absolutePath
    }

    fun stopRecording(): Int {
        val durationSeconds = ((System.currentTimeMillis() - recordingStartedAt) / 1_000L).toInt()

        recorder?.runCatching { stop() }
        releaseRecorder()

        return max(durationSeconds, 0)
    }

    fun startPlayback(
        filePath: String,
        onComplete: () -> Unit,
    ): Int {
        stopPlayback()

        val newPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
            setOnCompletionListener {
                stopPlayback()
                onComplete()
            }
            start()
        }

        player = newPlayer
        return newPlayer.duration.toSeconds()
    }

    fun stopPlayback() {
        player?.runCatching { stop() }
        player?.release()
        player = null
    }

    fun playbackPositionSeconds(): Int = player?.currentPosition?.toSeconds() ?: 0

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
    }
}

internal class PracticeRecordingAudioControllerFactory @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    fun create(): PracticeRecordingAudioController = PracticeRecordingAudioController(context)
}

private fun Int.toSeconds(): Int = this / 1_000
