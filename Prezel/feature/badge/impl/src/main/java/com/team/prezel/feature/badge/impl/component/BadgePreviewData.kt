package com.team.prezel.feature.badge.impl.component

import com.team.prezel.feature.badge.impl.contract.BadgeUiState
import com.team.prezel.feature.badge.impl.model.BadgeDetailUiModel

internal fun badgePreviewDetail() =
    BadgeDetailUiModel(
        badgeCode = "1",
        badgeName = "첫 발표",
        conditionText = "첫 발표 등록하기",
        detailDescription = "첫 발표를 등록하며 연습을 시작했어요.\n나의 발표 여정의 첫 걸음이에요.",
        imageUrl = "",
        isUnlocked = false,
    )

internal fun badgeScreenPreviewState() =
    BadgeUiState(
        badgeDetail = badgePreviewDetail(),
    )
