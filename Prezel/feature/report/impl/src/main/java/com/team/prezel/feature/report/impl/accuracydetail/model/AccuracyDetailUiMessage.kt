package com.team.prezel.feature.report.impl.accuracydetail.model

internal sealed interface AccuracyDetailUiMessage {
    data object FetchDetailFailed : AccuracyDetailUiMessage
}
