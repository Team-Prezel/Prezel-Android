package com.team.prezel.core.model.profile

data class User(
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val isProfileComplete: Boolean,
    val isTermsAgreement: Boolean,
) {
    val isRegistered: Boolean
        get() = isProfileComplete && isTermsAgreement
}
