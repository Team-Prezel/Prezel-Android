package com.team.prezel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.auth.AuthManager
import com.team.prezel.core.data.NetworkMonitor
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.domain.session.AuthSessionEventStream
import com.team.prezel.ui.PrezelApp
import com.team.prezel.ui.rememberPrezelAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.toImmutableSet
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var authSessionEventStream: AuthSessionEventStream

    @Inject
    lateinit var entryBuilders: Set<@JvmSuppressWildcards EntryProviderScope<NavKey>.() -> Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PrezelTheme {
                val appState = rememberPrezelAppState(
                    networkMonitor = networkMonitor,
                )

                PrezelApp(
                    appState = appState,
                    entryBuilders = entryBuilders.toImmutableSet(),
                    authSessionEventStream = authSessionEventStream,
                    onSessionExpired = authManager::clearCurrentProvider,
                )
            }
        }
    }
}
