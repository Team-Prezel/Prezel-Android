package com.team.prezel.core.designsystem.foundation.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class PrezelTypography(
    // Title
    // 페이지나 섹션의 주요 제목 등 핵심 정보를 강조하는 데 사용하는 스타일입니다.
    val title1Medium: TextStyle,
    val title1Bold: TextStyle,
    val title2Medium: TextStyle,
    val title2Bold: TextStyle,
    // Body
    // 일반 본문과 설명, 단일 문장 또는 단락 등의 주된 콘텐츠 전달에 사용하는 스타일입니다.
    val body1Regular: TextStyle,
    val body1Medium: TextStyle,
    val body1Bold: TextStyle,
    val body2Regular: TextStyle,
    val body2Medium: TextStyle,
    val body2Bold: TextStyle,
    val body3Regular: TextStyle,
    val body3Medium: TextStyle,
    val body3Bold: TextStyle,
    // Caption
    // 부가 정보, 안내, 설명, 작성 시간 등 메인 콘텐츠를 보조하는 짧은 정보를 전달할 때 사용하는 스타일입니다.
    val caption1Regular: TextStyle,
    val caption1Medium: TextStyle,
    val caption2Regular: TextStyle,
    val caption2Medium: TextStyle,
)
