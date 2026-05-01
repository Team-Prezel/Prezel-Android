package com.team.prezel.util

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import timber.log.Timber

internal class PrettyLoggerTree : Timber.DebugTree() {
    init {
        Logger.clearLogAdapters()
        Logger.addLogAdapter(
            AndroidLogAdapter(
                PrettyFormatStrategy
                    .newBuilder()
                    .showThreadInfo(false)
                    .methodCount(0)
                    .tag("PREZEL")
                    .build(),
            ),
        )
    }

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        Logger.log(priority, tag, message, t)
    }
}
