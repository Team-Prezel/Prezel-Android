package com.team.prezel.feature.profile.impl.model

internal enum class NicknameValidationState {
    Unchecked,
    Checking,
    Available,
    TooShort,
    TooLong,
    InvalidCharacter,
    Duplicated,
}
