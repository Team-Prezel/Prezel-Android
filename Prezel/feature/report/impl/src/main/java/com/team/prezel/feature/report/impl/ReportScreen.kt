package com.team.prezel.feature.report.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.contract.ReportUiState

@Composable
internal fun ReportScreen(
    modifier: Modifier = Modifier,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ReportScreen(
        uiState = uiState.value,
        modifier = modifier,
    )
}

@Composable
private fun ReportScreen(
    uiState: ReportUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (uiState) {
            ReportUiState.Loading -> Text(text = stringResource(R.string.feature_report_impl_loading))
            ReportUiState.Content -> Text(text = stringResource(R.string.feature_report_impl_title))
        }
    }
}

@BasicPreview
@Composable
private fun ReportScreenPreview() {
    PrezelTheme {
        ReportScreen(
            uiState = ReportUiState.Content,
        )
    }
}
