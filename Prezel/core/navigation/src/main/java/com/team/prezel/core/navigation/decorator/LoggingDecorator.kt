package com.team.prezel.core.navigation.decorator

import androidx.compose.runtime.DisposableEffect
import androidx.navigation3.runtime.NavEntryDecorator
import timber.log.Timber

private const val NAVIGATION_LOGGING_TAG = "Navigation"

class LoggingDecorator<T : Any> : NavEntryDecorator<T>(
    decorate = { entry ->
        DisposableEffect(entry.contentKey, entry.metadata) {
            Timber
                .tag(NAVIGATION_LOGGING_TAG)
                .d("[Navigation] SCREEN_ENTER | screen=${entry.contentKey} | metadata=${entry.metadata}")

            onDispose {
                Timber
                    .tag(NAVIGATION_LOGGING_TAG)
                    .d(message = "[Navigation] SCREEN_EXIT | screen=${entry.contentKey} | metadata=${entry.metadata}")
            }
        }

        entry.Content()
    },
)
