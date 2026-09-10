package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.domain.repository.fcm.FcmRepository
import com.team.prezel.core.network.datasource.FcmRemoteDataSource
import javax.inject.Inject

internal class FcmRepositoryImpl @Inject constructor(
    private val fcmRemoteDataSource: FcmRemoteDataSource,
) : FcmRepository {
    override suspend fun saveToken(token: String): Result<Unit> =
        runCatching {
            fcmRemoteDataSource.saveToken(token = token)
        }.mapDomainFailure()

    override suspend fun removeToken(token: String): Result<Unit> =
        runCatching {
            fcmRemoteDataSource.removeToken(token = token)
        }.mapDomainFailure()
}
