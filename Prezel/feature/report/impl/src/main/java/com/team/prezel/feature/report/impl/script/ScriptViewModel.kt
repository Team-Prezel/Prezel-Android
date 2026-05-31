package com.team.prezel.feature.report.impl.script

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.presentation.FetchPresentationScriptDetailUseCase
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import com.team.prezel.core.model.presentation.ScriptCorrection
import com.team.prezel.core.model.presentation.ScriptErrorType
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.report.impl.script.contract.ScriptUiEffect
import com.team.prezel.feature.report.impl.script.contract.ScriptUiIntent
import com.team.prezel.feature.report.impl.script.contract.ScriptUiState
import com.team.prezel.feature.report.impl.script.model.ScriptCorrectionUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ScriptViewModel.Factory::class)
internal class ScriptViewModel @AssistedInject constructor(
    @Assisted private val analysisResultId: Long,
    private val fetchPresentationScriptDetailUseCase: FetchPresentationScriptDetailUseCase,
) : BaseViewModel<ScriptUiState, ScriptUiIntent, ScriptUiEffect>(ScriptUiState()) {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted analysisResultId: Long,
        ): ScriptViewModel
    }

    init {
        fetchData(analysisResultId = analysisResultId)
    }

    override fun onIntent(intent: ScriptUiIntent) {
        when (intent) {
            ScriptUiIntent.ApplyAllCorrections -> applyAllCorrections()
            ScriptUiIntent.ClickCopy -> copyCurrentScriptToClipboard()
            ScriptUiIntent.DismissCorrectionPopup -> dismissCorrectionPopup()

            is ScriptUiIntent.ClickCorrection -> selectCorrection(
                correctionId = intent.correctionId,
                popupY = intent.popupY,
            )

            is ScriptUiIntent.ApplyCorrection -> applyCorrection(
                correctionId = intent.correctionId,
            )
        }
    }

    private fun selectCorrection(
        correctionId: Long,
        popupY: Int,
    ) {
        updateState {
            copy(
                selectedCorrectionId = correctionId,
                selectedCorrectionPopupY = popupY,
            )
        }
    }

    private fun dismissCorrectionPopup() {
        updateState {
            copy(selectedCorrectionId = null)
        }
    }

    private fun fetchData(analysisResultId: Long) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            // API 구현 이후 수정
            val detail = PresentationScriptDetail(
                originalScript =
                    """
                    안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.
                    저희 팀은 고승환, 박하영, 조민경, 최수빈, 한효주 총 5명으로 구성되어 있으며, 다음과 같은 목차로 발표 진행하겠습니다.
                    한 번쯤 발표하면서 긴장하신 경험 있으시죠.
                    오늘도 다들 긴장돼는 마음으로 오셨을 것 같습니다.
                    저희는 학교에서의 간단한 자기소개부터 회사의 성과보고까지 정말 다양하게, 그리고 정말 자주 발표를 경험합니다. 하지만 많은 발표를 해왔음에도 불구하고 발표를 생각했을 때 긴장하게 되는데요, 이처럼 발표를 앞둔 상황에서 경험하는 심리적 부담감을 발표 불안이라 합니다.
                    면접에서도, 학교에서도, 발표 능력을 기본 역량처럼 여기는 사회 분위기로 인해 이런 불안이 더해지고자 때문이었습니다.
                    가장 발표를 자주 경험하는 직장인을 예시로 들었을 때, “실수”에 대한 두려움을 발표 불안의 주된 원인으로 꼽았습니다.
                    즉, 발표와 가까운 환경에 사람들 조차 실수가 두려워 발표에 어려움을 겪고 있다는 말인데요, 이 때 사람들은 클래스 수강, 집단 상담 등 발표 불안을 이겨내기 위해 정말 다양한 시도를 하고 있었습니다.
                    특히 발표 코칭 학원을 등록하며 적극적인 대처를 취하는 사람들까지는 증가하고 있습니다.
                    그에 따라 개개인에게 가장 알맞은 발표 수업을 제공하며 발표 코칭 시장도 슬슬 자리를 잡고 있는데요, 그러나 대학생과 사회 초년생의 평균 수입과 비교했을 때 비싼 비용과 시간적 여유가 없어 지속적으로 수강하기 어렵다는 문제점이 있었습니다.
                    비용과 시간, 이런 고질적인 문제를 해결할 방법은 없을까요?
                    """.trimIndent(),
                scriptCorrections = listOf(
                    ScriptCorrection(
                        errorType = ScriptErrorType.SPELL,
                        sentence = "안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.",
                        originalText = "기달리는",
                        correctedText = "기다리는",
                        reason = "기달리다 → 기다리다",
                    ),
                    ScriptCorrection(
                        errorType = ScriptErrorType.GRAMMAR,
                        sentence = "오늘도 다들 긴장돼는 마음으로 오셨을 것 같습니다.",
                        originalText = "긴장돼는",
                        correctedText = "긴장되는",
                        reason = "되다 활용 오류",
                    ),
                    ScriptCorrection(
                        errorType = ScriptErrorType.GRAMMAR,
                        sentence = "그러나 대학생과 사회 초년생의 평균 수입과 비교했을 때 비싼 비용과 시간적 여유가 없어 지속적으로 수강하기 어렵다는 문제점이 있었습니다.",
                        originalText = "비싼 비용과 시간적 여유가 없어",
                        correctedText = "높은 비용과 부족한 시간적 여유로 인해",
                        reason = "주술 및 문장 연결 개선",
                    ),
                ),
            )

            updateState {
                copy(
                    isLoading = false,
                    originalScript = detail.originalScript,
                    currentScript = detail.originalScript,
                    scriptDetails = detail.toCorrectionUiModels(),
                )
            }
        }
    }

    private fun applyCorrection(correctionId: Long) {
        val updatedCorrections = currentState.scriptDetails
            .map { correction ->
                if (correction.id != correctionId) return@map correction
                if (correction.isApplied) return@map correction

                correction.copy(isApplied = true)
            }.toImmutableList()

        val updatedScript = rebuildScript(
            originalScript = currentState.originalScript,
            corrections = updatedCorrections,
        )

        updateState {
            copy(
                currentScript = updatedScript,
                scriptDetails = updatedCorrections,
                selectedCorrectionId = null,
            )
        }
    }

    private fun applyAllCorrections() {
        val updatedCorrections = currentState.scriptDetails
            .map { correction ->
                correction.copy(isApplied = true)
            }.toImmutableList()

        val updatedScript = rebuildScript(
            originalScript = currentState.originalScript,
            corrections = updatedCorrections,
        )

        updateState {
            copy(
                currentScript = updatedScript,
                scriptDetails = updatedCorrections,
            )
        }
    }

    private fun copyCurrentScriptToClipboard() {
        viewModelScope.launch {
            sendEffect(
                ScriptUiEffect.CurrentScriptCopyToClipBoard(
                    script = currentState.currentScript,
                ),
            )
        }
    }

    private fun PresentationScriptDetail.toCorrectionUiModels(): ImmutableList<ScriptCorrectionUiModel> {
        return scriptCorrections
            .mapIndexedNotNull { index, correction ->
                val sentenceStartIndex = originalScript.indexOf(correction.sentence)

                if (sentenceStartIndex == -1) return@mapIndexedNotNull null

                val sentenceEndIndex = sentenceStartIndex + correction.sentence.length

                val correctionStartIndex = originalScript.indexOf(
                    string = correction.originalText,
                    startIndex = sentenceStartIndex,
                )

                if (correctionStartIndex == -1) return@mapIndexedNotNull null
                if (correctionStartIndex >= sentenceEndIndex) return@mapIndexedNotNull null

                ScriptCorrectionUiModel(
                    id = index.toLong(),
                    errorType = correction.errorType,
                    sentence = correction.sentence,
                    originalText = correction.originalText,
                    correctedText = correction.correctedText,
                    reason = correction.reason,
                    originalRange = correctionStartIndex until correctionStartIndex + correction.originalText.length,
                )
            }.toImmutableList()
    }

    private fun rebuildScript(
        originalScript: String,
        corrections: List<ScriptCorrectionUiModel>,
    ): String {
        val appliedCorrections = corrections
            .filter { correction -> correction.isApplied }
            .sortedBy { correction -> correction.originalRange.first }

        val builder = StringBuilder(originalScript)
        var offset = 0

        appliedCorrections.forEach { correction ->
            val startIndex = correction.originalRange.first + offset
            val endIndex = correction.originalRange.last + 1 + offset

            builder.replace(
                startIndex,
                endIndex,
                correction.correctedText,
            )

            offset += correction.correctedText.length - correction.originalText.length
        }

        return builder.toString()
    }
}
