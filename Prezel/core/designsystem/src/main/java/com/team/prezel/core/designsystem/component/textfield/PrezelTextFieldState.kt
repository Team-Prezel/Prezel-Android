package com.team.prezel.core.designsystem.component.textfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach

/**
 * Prezel TextField의 상호작용 상태를 나타냅니다.
 *
 * TextField가 현재 어떤 사용자 입력 상태에 놓여 있는지를 정의하며,
 * 테두리 두께, 색상, 텍스트 색상 등의 스타일 계산에 사용됩니다.
 */
enum class PrezelTextFieldInteraction {
    /** 기본 상태 (포커스 및 입력이 없는 상태) */
    DEFAULT,

    /** 사용자가 입력 중인 상태 */
    TYPING,

    /** 입력이 완료된 상태 */
    TYPED,

    /** 비활성화된 상태 */
    DISABLED,
    ;

    companion object {
        fun calculate(
            enabled: Boolean,
            focused: Boolean,
            idleTyped: Boolean,
        ): PrezelTextFieldInteraction =
            when {
                !enabled -> PrezelTextFieldInteraction.DISABLED
                focused && !idleTyped -> PrezelTextFieldInteraction.TYPING
                focused && idleTyped -> PrezelTextFieldInteraction.TYPED
                else -> PrezelTextFieldInteraction.DEFAULT
            }
    }
}

/**
 * Prezel TextField 입력 결과에 대한 피드백 상태를 나타냅니다.
 *
 * 입력이 완료된 이후(`TYPED`) 상태에서 시각적인 피드백을 제공하기 위해 사용됩니다.
 */
@Stable
sealed interface PrezelTextFieldFeedback {
    val message: String

    /** 기본 상태 (피드백 없음) */
    data class DEFAULT(
        override val message: String,
    ) : PrezelTextFieldFeedback

    /** 긍정적인 입력 결과 */
    data class GOOD(
        override val message: String,
    ) : PrezelTextFieldFeedback

    /** 부정적인 입력 결과 */
    data class BAD(
        override val message: String,
    ) : PrezelTextFieldFeedback

    companion object {
        val NO_MESSAGE = DEFAULT(message = "")
    }
}

/**
 * Prezel TextField의 상태 기반 스타일 정보를 정의합니다.
 *
 * [interaction]과 [feedback] 조합을 통해 컨테이너 색상, 텍스트 색상,
 * 아이콘 색상, 테두리 스타일 등의 파생 스타일을 계산합니다.
 *
 * 이 객체는 UI 상태를 표현하는 값 객체로 사용되며,
 * 실제 색상 및 두께 계산 로직은 내부 함수에서 처리합니다.
 *
 * @property interaction TextField의 현재 상호작용 상태
 * @property feedback 입력 결과에 대한 피드백 상태
 */
@Immutable
data class PrezelTextFieldState(
    val interaction: PrezelTextFieldInteraction = PrezelTextFieldInteraction.DEFAULT,
    val feedback: PrezelTextFieldFeedback = PrezelTextFieldFeedback.NO_MESSAGE,
) {
    val supportingText: String = feedback.message

    /**
     * TextField 컨테이너의 배경 색상을 반환합니다.
     *
     * 입력 완료(`TYPED`) 상태에서만 피드백에 따라 배경 색상이 적용되며,
     * 그 외 상태에서는 투명한 색상을 반환합니다.
     */
    @Composable
    internal fun containerColor(colors: PrezelColors = PrezelTheme.colors): Color =
        when (interaction) {
            PrezelTextFieldInteraction.TYPED -> when (feedback) {
                is PrezelTextFieldFeedback.DEFAULT -> Color.Transparent
                is PrezelTextFieldFeedback.GOOD -> colors.feedbackGoodSmall
                is PrezelTextFieldFeedback.BAD -> colors.feedbackBadSmall
            }

            else -> Color.Transparent
        }

    /**
     * TextField 내부 텍스트 색상을 반환합니다.
     *
     * 상호작용 상태와 피드백 상태에 따라 텍스트 대비를 조정하며,
     * 다크 모드 여부에 따라 일부 색상이 달라질 수 있습니다.
     */
    @Composable
    internal fun textColor(
        colors: PrezelColors = PrezelTheme.colors,
        isDarkTheme: Boolean = isSystemInDarkTheme(),
    ): Color =
        when (interaction) {
            PrezelTextFieldInteraction.DEFAULT -> colors.textSmall
            PrezelTextFieldInteraction.DISABLED -> colors.textDisabled
            PrezelTextFieldInteraction.TYPING -> colors.textLarge
            PrezelTextFieldInteraction.TYPED -> when (feedback) {
                is PrezelTextFieldFeedback.DEFAULT -> colors.textRegular
                else -> if (isDarkTheme) PrezelColorScheme.Light.textLarge else colors.textLarge
            }
        }

    /**
     * TextField Supporting 텍스트 색상을 반환합니다.
     */
    @Composable
    internal fun supportingTextColor(colors: PrezelColors = PrezelTheme.colors): Color =
        when (interaction) {
            PrezelTextFieldInteraction.TYPED -> when (feedback) {
                is PrezelTextFieldFeedback.DEFAULT -> colors.textRegular
                is PrezelTextFieldFeedback.GOOD -> colors.feedbackGoodRegular
                is PrezelTextFieldFeedback.BAD -> colors.feedbackBadRegular
            }

            else -> PrezelTheme.colors.textRegular
        }

    /**
     * TextField 우측 트레일링 아이콘의 색상을 반환합니다.
     *
     * 비활성화 상태에서는 비활성 색상을 사용하며,
     * 입력 완료 상태에서는 피드백에 따라 아이콘 색상이 변경됩니다.
     */
    @Composable
    internal fun trailingIconColor(colors: PrezelColors = PrezelTheme.colors): Color =
        when (interaction) {
            PrezelTextFieldInteraction.DISABLED,
            PrezelTextFieldInteraction.DEFAULT,
            -> colors.iconDisabled

            PrezelTextFieldInteraction.TYPING -> colors.iconRegular
            PrezelTextFieldInteraction.TYPED -> when (feedback) {
                is PrezelTextFieldFeedback.DEFAULT -> colors.iconRegular
                is PrezelTextFieldFeedback.GOOD -> colors.interactiveRegular
                is PrezelTextFieldFeedback.BAD -> colors.feedbackBadRegular
            }
        }

    /**
     * TextField 테두리 스타일을 반환합니다.
     *
     * 상호작용 상태에 따라 테두리 두께가 달라지며,
     * 입력 완료 상태에서는 피드백에 따라 테두리 색상이 변경됩니다.
     */
    @Composable
    internal fun borderStroke(colors: PrezelColors = PrezelTheme.colors): BorderStroke {
        val borderWidth = when (interaction) {
            PrezelTextFieldInteraction.DEFAULT,
            PrezelTextFieldInteraction.DISABLED,
            -> 1.dp

            PrezelTextFieldInteraction.TYPING,
            PrezelTextFieldInteraction.TYPED,
            -> 2.dp
        }

        val borderColor = when (interaction) {
            PrezelTextFieldInteraction.DISABLED -> colors.borderDisabled
            PrezelTextFieldInteraction.DEFAULT -> colors.borderSmall
            PrezelTextFieldInteraction.TYPING -> colors.borderMedium
            PrezelTextFieldInteraction.TYPED -> when (feedback) {
                is PrezelTextFieldFeedback.DEFAULT -> colors.borderRegular
                is PrezelTextFieldFeedback.GOOD -> colors.interactiveRegular
                is PrezelTextFieldFeedback.BAD -> colors.feedbackBadRegular
            }
        }

        return BorderStroke(width = borderWidth, color = borderColor)
    }
}

@OptIn(FlowPreview::class)
@Composable
internal fun rememberPrezelTextFieldState(
    value: String,
    enabled: Boolean,
    focused: Boolean,
    feedback: PrezelTextFieldFeedback,
    idleMillis: Long = 1000L,
): PrezelTextFieldState {
    var idleTyped by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(focused) {
        if (!focused) idleTyped = false
    }

    LaunchedEffect(value, focused, enabled) {
        snapshotFlow { value }
            .distinctUntilChanged()
            .onEach { idleTyped = false }
            .debounce(idleMillis)
            .filter { focused && enabled }
            .collectLatest { idleTyped = true }
    }

    val interaction = remember(enabled, focused, idleTyped) {
        PrezelTextFieldInteraction.calculate(enabled = enabled, focused = focused, idleTyped = idleTyped)
    }

    return remember(interaction, feedback) { PrezelTextFieldState(interaction = interaction, feedback = feedback) }
}
