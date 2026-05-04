package com.team.prezel.core.network.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: Long,
    @SerialName("isProfileComplete")
    val isProfileComplete: Boolean,
    @SerialName("isTermsAgreement")
    val isTermsAgreement: Boolean,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("profileImgUrl")
    val profileImgUrl: ProfileImgUrl,
) {
    @Serializable
    data class ProfileImgUrl(
        @SerialName("isDefault")
        val isDefault: Boolean,
        @SerialName("url")
        val url: String?,
    )
}
