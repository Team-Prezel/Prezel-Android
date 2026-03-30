package com.team.prezel.core.designsystem.util

fun (() -> Unit).clickOnce(time: Long = 200L): () -> Unit =
    {
        ComposeMultipleEventCutter.processEvent(time) { this() }
    }

fun <T> ((T) -> Unit).clickOnce(time: Long = 200L): (T) -> Unit =
    { value ->
        ComposeMultipleEventCutter.processEvent(time) { this(value) }
    }

internal object ComposeMultipleEventCutter {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    fun processEvent(
        time: Long,
        event: () -> Unit,
    ) {
        if (now - lastEventTimeMs >= time) {
            event.invoke()
        }

        lastEventTimeMs = now
    }
}
