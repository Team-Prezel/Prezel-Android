package com.team.prezel.feature.login.impl.terms.component

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.login.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TermsDetailModal(
    url: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = onDismiss)

    Column(
        modifier = modifier
            .background(PrezelTheme.colors.bgRegular)
            .zIndex(1f)
            .fillMaxSize(),
    ) {
        PrezelTopAppBar(
            trailingIcons = {
                IconButton(onClick = onDismiss) {
                    Icon(
                        painter = painterResource(PrezelIcons.Cancel),
                        contentDescription = stringResource(R.string.feature_login_impl_cancel_icon_description),
                    )
                }
            },
        )
        NotionWebView(url = url, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun NotionWebView(
    url: String,
    modifier: Modifier = Modifier,
) {
    val webView = rememberWebView(url)

    AndroidView(
        modifier = modifier,
        factory = { webView },
    )
}

private const val NOTION_STYLE_PATCH = """
    (function() {
        var style = document.createElement('style');
        style.innerHTML = `
            html, body, * {
                overflow-y: auto !important;
                height: auto !important;
            }
        `;
        document.head.appendChild(style);
    })();
"""

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun rememberWebView(url: String): WebView {
    val context = LocalContext.current

    val webView = remember(url) {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(
                    view: WebView?,
                    loadedUrl: String?,
                ) {
                    val targetView = view ?: return
                    targetView.evaluateJavascript(NOTION_STYLE_PATCH.trimIndent(), null)
                }
            }

            loadUrl(url)
        }
    }

    DisposableEffect(url) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    return webView
}
