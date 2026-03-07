package com.team.prezel.core.navigation.decorator

import androidx.compose.runtime.DisposableEffect
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import timber.log.Timber

private const val NAVIGATION_LOGGING_TAG = "Navigation"

class LoggingDecorator<T : Any>(
    private val logger: Timber.Tree = Timber.tag(NAVIGATION_LOGGING_TAG),
) : NavEntryDecorator<T>(
        onPop = { key ->
            logger.d("[Navigation] BACKSTACK_REMOVE | screen=$key")
        },
        decorate = { entry ->
            DisposableEffect(entry.contentKey, entry.metadata) {
                logger.d(entry.enterLog())

                onDispose {
                    logger.d(entry.exitLog())
                }
            }

            entry.Content()
        },
    )

private fun <T : Any> NavEntry<T>.enterLog(): String = "[Navigation] SCREEN_ENTER | screen=$contentKey | metadata=$metadata"

private fun <T : Any> NavEntry<T>.exitLog(): String = "[Navigation] SCREEN_EXIT | screen=$contentKey | metadata=$metadata"
