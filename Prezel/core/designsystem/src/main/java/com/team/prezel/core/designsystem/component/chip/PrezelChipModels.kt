package com.team.prezel.core.designsystem.component.chip

import androidx.compose.runtime.Immutable

/**
 * 칩의 시각적 타입을 정의합니다.
 *
 * Chip의 배경, 테두리 표현 방식을 구분할 때 사용합니다.
 */
@Immutable
enum class PrezelChipType {
    /** 배경이 채워진 칩입니다. */
    FILLED,

    /** 테두리가 있는 칩입니다. */
    OUTLINED,
}

/**
 * 칩의 크기를 정의합니다.
 *
 * 칩의 높이, 패딩, 아이콘 크기, 텍스트 스타일의 기준으로 사용합니다.
 */
@Immutable
enum class PrezelChipSize {
    /** 작은 크기의 칩입니다. */
    SMALL,

    /** 기본 크기의 칩입니다. */
    REGULAR,
}

/**
 * 칩의 상호작용 상태를 정의합니다.
 *
 * 선택 여부나 비활성 상태에 따라 칩의 강조 수준을 표현할 때 사용합니다.
 */
@Immutable
enum class PrezelChipInteraction {
    /** 기본 상태의 칩입니다. */
    DEFAULT,

    /** 활성화되어 강조된 상태의 칩입니다. */
    ACTIVE,

    /** 비활성화된 상태의 칩입니다. */
    DISABLED,
}

/**
 * 칩의 피드백 상태를 정의합니다.
 *
 * 일반 상태 외에 경고성 의미를 함께 전달해야 할 때 사용합니다.
 */
@Immutable
enum class PrezelChipFeedback {
    /** 일반 상태의 칩입니다. */
    DEFAULT,

    /** 부정적 피드백을 표현하는 칩입니다. */
    BAD,
}
