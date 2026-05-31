package com.team.prezel.feature.report.impl.script.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.ScriptErrorType
import com.team.prezel.feature.report.impl.script.model.HighlightCorrectionUiModel
import com.team.prezel.feature.report.impl.script.model.ScriptCorrectionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

private const val CORRECTION_TAG = "CORRECTION"
private val POPUP_Y_OFFSET = 42.dp

@Composable
internal fun ScriptTextContent(
    script: String,
    corrections: ImmutableList<ScriptCorrectionUiModel>,
    onClickCorrection: (correctionId: Long, popupY: Int) -> Unit,
    modifier: Modifier = Modifier,
    spellHighlightStyle: SpanStyle = SpanStyle(
        color = PrezelTheme.colors.accentPurpleRegular,
        textDecoration = TextDecoration.Underline,
    ),
    grammarHighlightStyle: SpanStyle = SpanStyle(
        color = PrezelTheme.colors.accentTealRegular,
        textDecoration = TextDecoration.Underline,
    ),
) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var textPositionInWindow by remember { mutableStateOf(Offset.Zero) }
    val popupYOffset = with(androidx.compose.ui.platform.LocalDensity.current) { POPUP_Y_OFFSET.roundToPx() }
    val highlightCorrections = remember(corrections) { calculateHighlightCorrections(corrections) }
    val correctionMap = remember(corrections) { corrections.associateBy { it.id } }

    val annotatedText = remember(script, highlightCorrections, correctionMap) {
        buildAnnotatedText(
            script = script,
            highlightCorrections = highlightCorrections,
            correctionMap = correctionMap,
            spellHighlightStyle = spellHighlightStyle,
            grammarHighlightStyle = grammarHighlightStyle,
        )
    }

    Text(
        text = annotatedText,
        style = PrezelTheme.typography.body2Regular,
        color = PrezelTheme.colors.textLarge,
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                textPositionInWindow = coordinates.positionInWindow()
            }.correctionTapGesture(
                annotatedText = annotatedText,
                textLayoutResult = textLayoutResult,
                textPositionInWindow = textPositionInWindow,
                popupYOffset = popupYOffset,
                onClickCorrection = onClickCorrection,
            ),
        onTextLayout = { result -> textLayoutResult = result },
    )
}

private fun Modifier.correctionTapGesture(
    annotatedText: AnnotatedString,
    textLayoutResult: TextLayoutResult?,
    textPositionInWindow: Offset,
    popupYOffset: Int,
    onClickCorrection: (correctionId: Long, popupY: Int) -> Unit,
): Modifier =
    pointerInput(annotatedText, textLayoutResult, textPositionInWindow, popupYOffset) {
        detectTapGestures { offset ->
            val layoutResult = textLayoutResult ?: return@detectTapGestures
            val correctionId = findClickedCorrectionId(
                annotatedText = annotatedText,
                layoutResult = layoutResult,
                offset = offset,
            ) ?: return@detectTapGestures
            val position = layoutResult.getOffsetForPosition(offset)
            val boundingBox = layoutResult.getBoundingBox(position)
            val popupY = (textPositionInWindow.y + boundingBox.top).roundToInt() - popupYOffset

            onClickCorrection(correctionId, popupY)
        }
    }

private fun buildAnnotatedText(
    script: String,
    highlightCorrections: List<HighlightCorrectionUiModel>,
    correctionMap: Map<Long, ScriptCorrectionUiModel>,
    spellHighlightStyle: SpanStyle,
    grammarHighlightStyle: SpanStyle,
): AnnotatedString =
    buildAnnotatedString {
        var lastIndex = 0

        highlightCorrections.forEach { correction ->
            if (correction.range.first < lastIndex) return@forEach
            append(script.substring(lastIndex, correction.range.first))

            appendCorrectionText(
                script = script,
                correction = correction,
                correctionMap = correctionMap,
                spellHighlightStyle = spellHighlightStyle,
                grammarHighlightStyle = grammarHighlightStyle,
            )

            lastIndex = correction.range.last + 1
        }

        append(script.substring(lastIndex))
    }

private fun AnnotatedString.Builder.appendCorrectionText(
    script: String,
    correction: HighlightCorrectionUiModel,
    correctionMap: Map<Long, ScriptCorrectionUiModel>,
    spellHighlightStyle: SpanStyle,
    grammarHighlightStyle: SpanStyle,
) {
    val style = when (correctionMap[correction.correctionId]?.errorType) {
        ScriptErrorType.SPELLING -> spellHighlightStyle
        ScriptErrorType.GRAMMAR -> grammarHighlightStyle
        null -> null
    }

    val correctionText = script.substring(
        startIndex = correction.range.first,
        endIndex = correction.range.last + 1,
    )
    if (style == null) {
        append(correctionText)
        return
    }

    pushStringAnnotation(
        tag = CORRECTION_TAG,
        annotation = correction.correctionId.toString(),
    )
    withStyle(style) {
        append(correctionText)
    }
    pop()
}

private fun findClickedCorrectionId(
    annotatedText: AnnotatedString,
    layoutResult: TextLayoutResult,
    offset: Offset,
): Long? {
    val position = layoutResult.getOffsetForPosition(offset)
    return annotatedText
        .getStringAnnotations(CORRECTION_TAG, position, position)
        .firstOrNull()
        ?.item
        ?.toLongOrNull()
}

private fun calculateHighlightCorrections(corrections: List<ScriptCorrectionUiModel>): List<HighlightCorrectionUiModel> {
    val sortedCorrections = corrections.sortedBy { correction ->
        correction.originalRange.first
    }

    var offset = 0
    val highlightCorrections = mutableListOf<HighlightCorrectionUiModel>()

    sortedCorrections.forEach { correction ->
        val currentStartIndex = correction.originalRange.first + offset
        val currentEndIndex = correction.originalRange.last + offset

        if (!correction.isApplied) {
            highlightCorrections += HighlightCorrectionUiModel(
                correctionId = correction.id,
                range = currentStartIndex..currentEndIndex,
            )
        }

        if (correction.isApplied) {
            offset += correction.correctedText.length - correction.originalText.length
        }
    }

    return highlightCorrections
}

@BasicPreview
@Composable
private fun ScriptTextContentPreview() {
    PrezelTheme {
        ScriptTextContent(
            script =
                """
                안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.
                오늘도 다들 긴장돼는 마음으로 오셨을 것 같습니다.
                면접에서도, 학교에서도, 발표 능력을 기본 역량처럼 여기는 사회 분위기로 인해 이런 불안이 더해지고자 때문이었습니다.
                """.trimIndent(),
            corrections = listOf(
                ScriptCorrectionUiModel(
                    id = 0L,
                    errorType = ScriptErrorType.SPELLING,
                    sentence = "안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.",
                    originalText = "기달리는",
                    correctedText = "기다리는",
                    reason = "표준어는 '기다리다'를 활용한 표현이에요.",
                    originalRange = 34 until 38,
                ),
                ScriptCorrectionUiModel(
                    id = 1L,
                    errorType = ScriptErrorType.GRAMMAR,
                    sentence = "오늘도 다들 긴장돼는 마음으로 오셨을 것 같습니다.",
                    originalText = "긴장돼는",
                    correctedText = "긴장되는",
                    reason = "보조 용언 활용을 바로잡으면 문장이 자연스러워져요.",
                    originalRange = 56 until 60,
                ),
            ).toPersistentList(),
            onClickCorrection = { _, _ -> },
        )
    }
}
