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
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.report.api.ReportEntrySource
import com.team.prezel.feature.report.api.ReportNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureAnalysisEntryBuilder() {
    analysisEntry<AnalysisNavKey.Schedule> { key -> key.enterStep(AnalysisFlowStep.PRESENTATION_SCHEDULE) }
    analysisEntry<AnalysisNavKey.Situation> { key -> key.enterStep(AnalysisFlowStep.PRESENTATION_SITUATION) }
    analysisEntry<AnalysisNavKey.Script> { key -> key.enterStep(AnalysisFlowStep.SCRIPT_INPUT) }
    analysisEntry<AnalysisNavKey.AudioUpload> { key -> key.enterStep(AnalysisFlowStep.AUDIO_UPLOAD) }
    analysisEntry<AnalysisNavKey.Recording> { key -> key.enterStep(AnalysisFlowStep.VOICE_RECORDING) }
    analysisEntry<AnalysisNavKey.Analyzing> { key -> key.enterStep(AnalysisFlowStep.ANALYZING) }
    analysisEntry<AnalysisNavKey.ReRecording> { key ->
        AnalysisFlowUiIntent.StartReRecording(
            presentationId = key.presentationId,
            isPast = key.isPast,
        )
    }
    analysisEntry<AnalysisNavKey.ReWritingScript> { key ->
        AnalysisFlowUiIntent.StartReWritingScript(
            presentationId = key.presentationId,
            isPast = key.isPast,
        )
    }
}

private fun AnalysisNavKey.enterStep(step: AnalysisFlowStep): AnalysisFlowUiIntent =
    AnalysisFlowUiIntent.EnterStep(
        step = step,
        startType = startType,
    )

private inline fun <reified T : AnalysisNavKey> EntryProviderScope<NavKey>.analysisEntry(crossinline enterIntent: (T) -> AnalysisFlowUiIntent) {
    entry<T> { key ->
        AnalysisRoute(
            navKey = key,
            enterIntent = enterIntent(key),
            stepToNavKey = key::toNavKey,
        )
    }
}

@Composable
private fun AnalysisRoute(
    navKey: AnalysisNavKey,
    enterIntent: AnalysisFlowUiIntent,
    stepToNavKey: (AnalysisFlowStep) -> AnalysisNavKey,
) {
    val navigator = LocalNavigator.current
    val viewModelStoreOwner = LocalContext.current.findViewModelStoreOwner()
    val viewModel = hiltViewModel<AnalysisFlowViewModel, AnalysisFlowViewModel.Factory>(
        viewModelStoreOwner = viewModelStoreOwner,
        key = navKey.flowId,
        creationCallback = { factory -> factory.create(navKey is AnalysisNavKey.ReWritingScript) },
    )

    LaunchedEffect(enterIntent) {
        viewModel.onIntent(enterIntent)
    }

    AnalysisScreen(
        onBack = { navigator.goBack() },
        navigateToStep = { step, clearStack ->
            navigator.navigate(
                key = stepToNavKey(step),
                clearStack = clearStack,
            )
        },
        navigateToReport = { presentationId ->
            navigator.navigate(
                key = ReportNavKey(
                    presentationId = presentationId,
                    entrySource = ReportEntrySource.ANALYSIS,
                ),
                clearStack = true,
            )
        },
        navigateToHome = {
            navigator.replaceRoot(HomeNavKey)
        },
        viewModel = viewModel,
    )
}

private fun AnalysisNavKey.toNavKey(step: AnalysisFlowStep): AnalysisNavKey =
    when (step) {
        AnalysisFlowStep.PRESENTATION_SCHEDULE -> AnalysisNavKey.Schedule(flowId = flowId, startType = startType)
        AnalysisFlowStep.PRESENTATION_SITUATION -> AnalysisNavKey.Situation(flowId = flowId, startType = startType)
        AnalysisFlowStep.SCRIPT_INPUT,
        AnalysisFlowStep.SCRIPT_FILE_RECOGNITION_FAILED,
        -> AnalysisNavKey.Script(flowId = flowId, startType = startType)

        AnalysisFlowStep.AUDIO_UPLOAD -> AnalysisNavKey.AudioUpload(flowId = flowId, startType = startType)
        AnalysisFlowStep.VOICE_RECORDING,
        AnalysisFlowStep.ANALYSIS_FAILED,
        AnalysisFlowStep.FILE_RECOGNITION_FAILED,
        -> AnalysisNavKey.Recording(flowId = flowId, startType = startType)

        AnalysisFlowStep.ANALYZING -> AnalysisNavKey.Analyzing(flowId = flowId, startType = startType)
    }

private tailrec fun Context.findViewModelStoreOwner(): ViewModelStoreOwner =
    when (this) {
        is ViewModelStoreOwner -> this
        is ContextWrapper -> baseContext.findViewModelStoreOwner()
        else -> error("Context에서 ViewModelStoreOwner를 찾을 수 없습니다: $this")
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
