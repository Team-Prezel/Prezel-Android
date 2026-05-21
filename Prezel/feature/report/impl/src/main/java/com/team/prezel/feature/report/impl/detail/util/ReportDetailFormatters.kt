package com.team.prezel.feature.report.impl.detail.util

import com.team.prezel.feature.report.impl.detail.model.PresentationInfoUiModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal fun Instant.toReportDateLabel(): String =
    toLocalDateTime(TimeZone.currentSystemDefault()).date.let { date ->
        "${date.year}년 ${date.month.number}월 ${date.day}일"
    }

internal fun PresentationInfoUiModel.dateLabel(): String = analyzedAt.toReportDateLabel()

internal fun PresentationInfoUiModel.durationLabel(): String = formattedDuration
