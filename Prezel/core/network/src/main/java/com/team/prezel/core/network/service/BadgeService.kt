package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.badge.GetBadgeDetailResponse
import com.team.prezel.core.network.model.badge.GetBadgeResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface BadgeService {
    @GET("badges")
    suspend fun getBadges(
        @Query("sort") sort: String,
    ): BaseResponse<List<GetBadgeResponse>>

    @GET("badges/{badgeCode}")
    suspend fun getBadgeDetail(
        @Path("badgeCode") badgeCode: String,
    ): BaseResponse<GetBadgeDetailResponse>
}
