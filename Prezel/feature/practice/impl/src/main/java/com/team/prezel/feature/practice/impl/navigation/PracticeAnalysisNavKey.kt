package com.team.prezel.feature.practice.impl.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal data class PracticeAnalysisNavKey(
    val recordingFilePath: String,
    val referenceText: String,
) : NavKey
