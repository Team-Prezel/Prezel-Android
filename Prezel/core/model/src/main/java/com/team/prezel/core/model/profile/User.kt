package com.team.prezel.core.model.profile

data class User(
    val id: Long,
    val email: String,
    val nickname: Nickname,
    val profileImage: ProfileImage,
    val isRegistered: Boolean,
) {
    data class ProfileImage(
        val url: String,
        val isDefault: Boolean,
    )
}
