package com.team.prezel.core.designsystem.component.chip.config

import androidx.compose.runtime.Immutable

/**
 * 칩의 시각적 타입을 정의합니다.
 *
 * Chip의 배경, 테두리 표현 방식을 구분할 때 사용합니다.
 */
@Immutable
enum class PrezelChipType {
    FILLED,
    OUTLINED,
}

/**
 * 칩의 크기를 정의합니다.
 *
 * 칩의 높이, 패딩, 아이콘 크기, 텍스트 스타일의 기준으로 사용합니다.
 */
@Immutable
enum class PrezelChipSize {
    SMALL,
    REGULAR,
}

/**
 * 칩의 시각 상태를 정의합니다.
 *
 * 기본, 강조, 비활성 상태에 따라 칩의 강조 수준을 표현할 때 사용합니다.
 */
@Immutable
enum class PrezelChipState {
    DEFAULT,
    ACTIVE,
    DISABLED,
}

/**
 * 칩의 의미 상태를 정의합니다.
 *
 * 일반 상태 외에 부정/경고 의미를 함께 전달해야 할 때 사용합니다.
 */
@Immutable
enum class PrezelChipStatus {
    DEFAULT,
    BAD,
    WARNING,
}

/**
 * 일반 칩의 시각적 계층을 정의합니다.
 *
 * 동일한 타입 안에서 정보의 우선순위를 표현할 때 사용합니다.
 */
@Immutable
enum class PrezelChipHierarchy {
    PRIMARY,
    SECONDARY,
}

/**
 * 일반 칩의 강조 색상을 정의합니다.
 *
 * 특정 의미를 컬러로 강조해야 할 때 사용합니다.
 */
@Immutable
enum class PrezelChipAccent {
    DEFAULT,
    PURPLE,
    TEAL,
}
