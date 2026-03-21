package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 버튼의 시각적 타입을 정의합니다.
 *
 * 버튼의 배경, 테두리, 강조 방식에 따라 스타일을 구분할 때 사용합니다.
 */
@Immutable
enum class ButtonType {
    /** 배경이 채워진 버튼입니다. */
    FILLED,

    /** 테두리가 있는 버튼입니다. */
    OUTLINED,

    /** 배경과 테두리가 없는 버튼입니다. */
    GHOST,
}

/**
 * 버튼의 크기를 정의합니다.
 *
 * 버튼의 높이, 패딩, 아이콘 크기, 텍스트 스타일 등의 기준으로 사용합니다.
 */
@Immutable
enum class ButtonSize {
    /** 가장 작은 크기의 버튼입니다. */
    XSMALL,

    /** 작은 크기의 버튼입니다. */
    SMALL,

    /** 기본 크기의 버튼입니다. */
    REGULAR,
}

/**
 * 버튼의 계층 구조를 정의합니다.
 *
 * 액션의 중요도와 강조 수준에 따라 버튼의 우선순위를 표현할 때 사용합니다.
 */
@Immutable
enum class ButtonHierarchy {
    /** 가장 중요한 주요 액션에 사용되는 계층입니다. */
    PRIMARY,

    /** 보조 액션에 사용되는 계층입니다. */
    SECONDARY,
}

@Composable
internal fun Modifier.buttonContentPadding(size: ButtonSize): Modifier =
    this.padding(
        horizontal = when (size) {
            ButtonSize.XSMALL -> PrezelTheme.spacing.V10
            ButtonSize.SMALL -> PrezelTheme.spacing.V12
            ButtonSize.REGULAR -> PrezelTheme.spacing.V16
        },
        vertical = when (size) {
            ButtonSize.XSMALL -> PrezelTheme.spacing.V6
            ButtonSize.SMALL -> PrezelTheme.spacing.V8
            ButtonSize.REGULAR -> PrezelTheme.spacing.V12
        },
    )

@Composable
internal fun Modifier.iconButtonContentPadding(size: ButtonSize): Modifier =
    this.padding(
        all = when (size) {
            ButtonSize.XSMALL -> PrezelTheme.spacing.V8
            ButtonSize.SMALL -> PrezelTheme.spacing.V10
            ButtonSize.REGULAR -> PrezelTheme.spacing.V14
        },
    )
