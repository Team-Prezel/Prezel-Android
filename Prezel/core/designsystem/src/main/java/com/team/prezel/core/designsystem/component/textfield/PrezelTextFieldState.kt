package com.team.prezel.core.designsystem.component.textfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.color.PrezelColors
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * Prezel TextField의 State를 나타냅니다.
 *
 * TextField가 현재 어떤 사용자 입력 상태에 놓여 있는지를 정의하며,
 * 테두리 두께, 색상, 텍스트 색상 등의 스타일 계산에 사용됩니다.
 */
enum class PrezelTextFieldState {
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
            hasValue: Boolean,
        ): PrezelTextFieldState =
            when {
                !enabled -> DISABLED
                focused -> TYPING
                hasValue -> TYPED
                else -> DEFAULT
            }
    }
}

/**
 * Prezel TextField 입력 결과에 대한 Status를 나타냅니다.
 *
 * 입력이 완료된 이후(`TYPED`) 상태에서 시각적인 Status를 제공하기 위해 사용됩니다.
 */
@Stable
sealed interface PrezelTextFieldStatus {
    val message: String

    /** 기본 Status */
    data class Default(
        override val message: String,
    ) : PrezelTextFieldStatus

    /** 긍정적인 입력 결과 */
    data class Good(
        override val message: String,
    ) : PrezelTextFieldStatus

    /** 부정적인 입력 결과 */
    data class Bad(
        override val message: String,
    ) : PrezelTextFieldStatus

    companion object {
        val DEFAULT = Default(message = "")
    }
}

/**
 * Prezel TextField의 상태 기반 스타일 정보를 정의합니다.
 *
 * [state]와 [status] 조합을 통해 컨테이너 색상, 텍스트 색상,
 * 아이콘 색상, 테두리 스타일 등의 파생 스타일을 계산합니다.
 *
 * 이 객체는 UI 상태를 표현하는 값 객체로 사용되며,
 * 실제 색상 및 두께 계산 로직은 내부 함수에서 처리합니다.
 *
 * @property state TextField의 현재 State
 * @property status 입력 결과에 대한 Status
 */
@Immutable
data class PrezelTextFieldStyle(
    val state: PrezelTextFieldState = PrezelTextFieldState.DEFAULT,
    val status: PrezelTextFieldStatus = PrezelTextFieldStatus.DEFAULT,
) {
    val supportingText: String = status.message

    /**
     * TextField 컨테이너의 배경 색상을 반환합니다.
     *
     * 입력 중(`TYPING`) 또는 입력 완료(`TYPED`) 상태에서 Status에 따라 배경 색상이 적용되며,
     * 그 외 상태에서는 투명한 색상을 반환합니다.
     */
    @Composable
    internal fun containerColor(colors: PrezelColors = PrezelTheme.colors): Color =
        when (state) {
            PrezelTextFieldState.DEFAULT,
            PrezelTextFieldState.DISABLED,
            -> Color.Transparent

            PrezelTextFieldState.TYPING,
            PrezelTextFieldState.TYPED,
            -> when (status) {
                is PrezelTextFieldStatus.Default -> Color.Transparent
                is PrezelTextFieldStatus.Good -> colors.feedbackGoodSmall
                is PrezelTextFieldStatus.Bad -> colors.feedbackBadSmall
            }
        }

    /**
     * TextField 내부 텍스트 색상을 반환합니다.
     *
     * State와 Status에 따라 텍스트 대비를 조정하며,
     * 다크 모드 여부에 따라 일부 색상이 달라질 수 있습니다.
     */
    @Composable
    internal fun textColor(colors: PrezelColors = PrezelTheme.colors): Color =
        when (state) {
            PrezelTextFieldState.DISABLED -> colors.textDisabled
            PrezelTextFieldState.DEFAULT -> colors.textSmall
            PrezelTextFieldState.TYPING -> colors.textLarge
            PrezelTextFieldState.TYPED -> colors.textLarge
        }

    /**
     * TextField Supporting 텍스트 색상을 반환합니다.
     */
    @Composable
    internal fun supportingTextColor(colors: PrezelColors = PrezelTheme.colors): Color =
        when (state) {
            PrezelTextFieldState.DISABLED -> colors.textDisabled
            PrezelTextFieldState.DEFAULT -> colors.textRegular
            PrezelTextFieldState.TYPING -> when (status) {
                is PrezelTextFieldStatus.Default -> colors.textRegular
                is PrezelTextFieldStatus.Good -> colors.feedbackGoodRegular
                is PrezelTextFieldStatus.Bad -> colors.feedbackBadRegular
            }

            PrezelTextFieldState.TYPED -> when (status) {
                is PrezelTextFieldStatus.Default -> colors.textMedium
                is PrezelTextFieldStatus.Good -> colors.feedbackGoodRegular
                is PrezelTextFieldStatus.Bad -> colors.feedbackBadRegular
            }
        }

    /**
     * TextField 우측 트레일링 아이콘의 색상을 반환합니다.
     *
     * 비활성화 상태에서는 비활성 색상을 사용하며,
     * 입력 완료 상태에서는 Status에 따라 아이콘 색상이 변경됩니다.
     */
    @Composable
    internal fun trailingIconColor(colors: PrezelColors = PrezelTheme.colors): Color =
        when (state) {
            PrezelTextFieldState.DISABLED,
            PrezelTextFieldState.DEFAULT,
            -> colors.iconDisabled

            PrezelTextFieldState.TYPING,
            PrezelTextFieldState.TYPED,
            -> when (status) {
                is PrezelTextFieldStatus.Default -> colors.iconRegular
                is PrezelTextFieldStatus.Good -> colors.interactiveRegular
                is PrezelTextFieldStatus.Bad -> colors.feedbackBadRegular
            }
        }

    /**
     * TextField 테두리 스타일을 반환합니다.
     *
     * State에 따라 테두리 두께가 달라지며,
     * 입력 완료 상태에서는 Status에 따라 테두리 색상이 변경됩니다.
     */
    @Composable
    internal fun borderStroke(colors: PrezelColors = PrezelTheme.colors): BorderStroke {
        val borderWidth = when (state) {
            PrezelTextFieldState.DEFAULT,
            PrezelTextFieldState.DISABLED,
            -> 1.dp

            PrezelTextFieldState.TYPING,
            PrezelTextFieldState.TYPED,
            -> 2.dp
        }

        val borderColor = when (state) {
            PrezelTextFieldState.DISABLED -> colors.borderDisabled
            PrezelTextFieldState.DEFAULT -> colors.borderSmall
            PrezelTextFieldState.TYPING -> when (status) {
                is PrezelTextFieldStatus.Default -> colors.borderMedium
                is PrezelTextFieldStatus.Good -> colors.feedbackGoodRegular
                is PrezelTextFieldStatus.Bad -> colors.feedbackBadRegular
            }

            PrezelTextFieldState.TYPED -> when (status) {
                is PrezelTextFieldStatus.Default -> colors.borderRegular
                is PrezelTextFieldStatus.Good -> colors.feedbackGoodRegular
                is PrezelTextFieldStatus.Bad -> colors.feedbackBadRegular
            }
        }

        return BorderStroke(width = borderWidth, color = borderColor)
    }
}

@Composable
internal fun rememberPrezelTextFieldState(
    value: String,
    enabled: Boolean,
    focused: Boolean,
): PrezelTextFieldState =
    PrezelTextFieldState.calculate(
        enabled = enabled,
        focused = focused,
        hasValue = value.isNotEmpty(),
    )

internal fun applyPrezelTextInputPolicy(
    value: String,
    maxLength: Int,
): String {
    require(maxLength >= 0) { "maxLength must be >= 0" }
    return value
        .replace("\n", "")
        .take(maxLength)
}
