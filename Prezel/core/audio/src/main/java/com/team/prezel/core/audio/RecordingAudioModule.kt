package com.team.prezel.core.audio

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RecordingAudioModule {
    @Binds
    @ViewModelScoped
    abstract fun bindRecordingAudioController(impl: MediaRecordingAudioController): RecordingAudioController
}
