package com.team.prezel.core.designsystem.foundation.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class PrezelColors(
    // Interactive
    // 브랜드 색으로 활성화 상태 및 강조가 필요한 요소에 사용하는 색상입니다.
    val interactiveXSmall: Color,
    val interactiveSmall: Color,
    val interactiveRegular: Color,
    // Background
    // 콘텐츠의 구조와 계층을 구분하는 색상입니다.
    val bgRegular: Color,
    val bgMedium: Color,
    val bgLarge: Color,
    val bgScrim: Color,
    val bgDisabled: Color,
    // Text
    // 정보와 콘텐츠를 명확하게 전달하기 위해 사용하는 색상입니다.
    val textSmall: Color,
    val textRegular: Color,
    val textMedium: Color,
    val textLarge: Color,
    val textDisabled: Color,
    // Icon
    // 기능적 액션과 시각적 커뮤니케이션을 돕기 위해 사용하는 색상입니다.
    val iconRegular: Color,
    val iconMedium: Color,
    val iconLarge: Color,
    val iconDisabled: Color,
    // Border
    // 콘텐츠의 영역과 구조를 구분하는 색상입니다.
    val borderSmall: Color,
    val borderRegular: Color,
    val borderMedium: Color,
    val borderLarge: Color,
    val borderDisabled: Color,
    // Feedback
    // 성공, 오류, 경고 상황을 명확하게 전달하기 위해 의미별로 정의된 색상입니다.
    val feedbackGoodSmall: Color,
    val feedbackGoodRegular: Color,
    val feedbackBadSmall: Color,
    val feedbackBadRegular: Color,
    val feedbackWarningSmall: Color,
    val feedbackWarningRegular: Color,
    // Accent
    // 콘텐츠의 주목성과 인지도를 높이기 위해 버튼, 액션, 강조 요소 등에 사용되는 포인트 색상입니다.
    val accentPurpleSmall: Color,
    val accentPurpleRegular: Color,
    val accentMagentaSmall: Color,
    val accentMagentaRegular: Color,
    // Solid
    // 흰색과 검정색을 제공하여 시각적 대비, 보조, 구분 등에 활용되는 절대값 색상입니다.
    val solidWhite: Color,
    val solidBlack: Color,
)
