package com.team.prezel.feature.analysis.impl.di

import com.team.prezel.feature.analysis.impl.cache.AnalysisFileCache
import com.team.prezel.feature.analysis.impl.cache.AnalysisFileCacheImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface AnalysisModule {
    @Binds
    fun bindAnalysisFileCache(impl: AnalysisFileCacheImpl): AnalysisFileCache
}
