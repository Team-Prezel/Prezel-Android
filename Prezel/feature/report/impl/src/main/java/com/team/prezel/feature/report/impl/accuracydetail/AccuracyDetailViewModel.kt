package com.team.prezel.feature.report.impl.accuracydetail

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationWordDetailUseCase
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.report.impl.accuracydetail.contract.AccuracyDetailUiEffect
import com.team.prezel.feature.report.impl.accuracydetail.contract.AccuracyDetailUiIntent
import com.team.prezel.feature.report.impl.accuracydetail.contract.AccuracyDetailUiState
import com.team.prezel.feature.report.impl.accuracydetail.model.AccuracyDetailUiMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AccuracyDetailViewModel.Factory::class)
internal class AccuracyDetailViewModel @AssistedInject constructor(
    @Assisted private val analysisResultId: Long,
    private val fetchPresentationWordDetailUseCase: FetchPresentationWordDetailUseCase,
) : BaseViewModel<AccuracyDetailUiState, AccuracyDetailUiIntent, AccuracyDetailUiEffect>(
        AccuracyDetailUiState.Loading,
    ) {
    @AssistedFactory
    interface Factory {
        fun create(analysisResultId: Long): AccuracyDetailViewModel
    }

    init {
        fetchDetails()
    }

    override fun onIntent(intent: AccuracyDetailUiIntent) = Unit

    private fun fetchDetails() {
        viewModelScope.launch {
            val nextState = runCatching {
                AccuracyDetailUiState.Content(
                    wordDetail = fetchPresentationWordDetailUseCase(analysisResultId).getOrThrow(),
                )
            }.getOrElse {
                sendEffect(AccuracyDetailUiEffect.ShowMessage(AccuracyDetailUiMessage.FetchDetailFailed))
                AccuracyDetailUiState.Error
            }
            updateState { nextState }
        }
    }
}
