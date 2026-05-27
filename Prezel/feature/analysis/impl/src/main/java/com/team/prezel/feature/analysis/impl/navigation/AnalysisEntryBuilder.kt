package com.team.prezel.feature.analysis.impl.navigation

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.analysis.api.AnalysisNavKey
import com.team.prezel.feature.analysis.impl.AnalysisFlowViewModel
import com.team.prezel.feature.analysis.impl.AnalysisScreen
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiIntent
import com.team.prezel.feature.report.api.ReportNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureAnalysisEntryBuilder() {
    entry<AnalysisNavKey.Schedule> { key ->
        AnalysisRoute(
            flowId = key.flowId,
            enterIntent = AnalysisFlowUiIntent.EnterStep(AnalysisFlowStep.PRESENTATION_SCHEDULE),
            stepToNavKey = key::toNavKey,
        )
    }
    entry<AnalysisNavKey.Situation> { key ->
        AnalysisRoute(
            flowId = key.flowId,
            enterIntent = AnalysisFlowUiIntent.EnterStep(AnalysisFlowStep.PRESENTATION_SITUATION),
            stepToNavKey = key::toNavKey,
        )
    }
    entry<AnalysisNavKey.Script> { key ->
        AnalysisRoute(
            flowId = key.flowId,
            enterIntent = AnalysisFlowUiIntent.EnterStep(AnalysisFlowStep.SCRIPT_INPUT),
            stepToNavKey = key::toNavKey,
        )
    }
    entry<AnalysisNavKey.AudioUpload> { key ->
        AnalysisRoute(
            flowId = key.flowId,
            enterIntent = AnalysisFlowUiIntent.EnterStep(AnalysisFlowStep.AUDIO_UPLOAD),
            stepToNavKey = key::toNavKey,
        )
    }
    entry<AnalysisNavKey.Recording> { key ->
        AnalysisRoute(
            flowId = key.flowId,
            enterIntent = AnalysisFlowUiIntent.EnterStep(AnalysisFlowStep.VOICE_RECORDING),
            stepToNavKey = key::toNavKey,
        )
    }
    entry<AnalysisNavKey.Analyzing> { key ->
        AnalysisRoute(
            flowId = key.flowId,
            enterIntent = AnalysisFlowUiIntent.EnterStep(AnalysisFlowStep.ANALYZING),
            stepToNavKey = key::toNavKey,
        )
    }
    entry<AnalysisNavKey.ReRecording> { key ->
        AnalysisRoute(
            flowId = key.flowId,
            enterIntent = AnalysisFlowUiIntent.StartReRecording(
                presentationId = key.presentationId,
                isPast = key.isPast,
            ),
            stepToNavKey = key::toNavKey,
        )
    }
    entry<AnalysisNavKey.ReWritingScript> { key ->
        AnalysisRoute(
            flowId = key.flowId,
            enterIntent = AnalysisFlowUiIntent.StartReWritingScript(
                presentationId = key.presentationId,
                isPast = key.isPast,
            ),
            stepToNavKey = key::toNavKey,
        )
    }
}

@Composable
private fun AnalysisRoute(
    flowId: String,
    enterIntent: AnalysisFlowUiIntent,
    stepToNavKey: (AnalysisFlowStep) -> AnalysisNavKey,
) {
    val navigator = LocalNavigator.current
    val viewModelStoreOwner = LocalContext.current.findViewModelStoreOwner()
    val viewModel = hiltViewModel<AnalysisFlowViewModel>(
        viewModelStoreOwner = viewModelStoreOwner,
        key = flowId,
    )

    LaunchedEffect(enterIntent) {
        viewModel.onIntent(enterIntent)
    }

    AnalysisScreen(
        onBack = { navigator.goBack() },
        navigateToStep = { step -> navigator.navigate(key = stepToNavKey(step)) },
        navigateToReport = { presentationId ->
            navigator.navigate(
                key = ReportNavKey(presentationId = presentationId),
                clearStack = true,
            )
        },
        viewModel = viewModel,
    )
}

private fun AnalysisNavKey.toNavKey(step: AnalysisFlowStep): AnalysisNavKey =
    when (step) {
        AnalysisFlowStep.PRESENTATION_SCHEDULE -> AnalysisNavKey.Schedule(flowId = flowId)
        AnalysisFlowStep.PRESENTATION_SITUATION -> AnalysisNavKey.Situation(flowId = flowId)
        AnalysisFlowStep.SCRIPT_INPUT,
        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
        -> AnalysisNavKey.Script(flowId = flowId)

        AnalysisFlowStep.AUDIO_UPLOAD -> AnalysisNavKey.AudioUpload(flowId = flowId)
        AnalysisFlowStep.VOICE_RECORDING,
        AnalysisFlowStep.FILE_RECOGNITION_FAILED,
        -> AnalysisNavKey.Recording(flowId = flowId)

        AnalysisFlowStep.ANALYZING -> AnalysisNavKey.Analyzing(flowId = flowId)
    }

private tailrec fun Context.findViewModelStoreOwner(): ViewModelStoreOwner =
    when (this) {
        is ViewModelStoreOwner -> this
        is ContextWrapper -> baseContext.findViewModelStoreOwner()
        else -> error("ViewModelStoreOwner is not available from context: $this")
    }

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureAnalysisModule {
    @IntoSet
    @Provides
    fun provideFeatureAnalysisEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureAnalysisEntryBuilder()
        }
}
