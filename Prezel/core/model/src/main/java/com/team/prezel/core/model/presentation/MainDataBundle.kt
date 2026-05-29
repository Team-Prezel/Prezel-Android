package com.team.prezel.core.model.presentation

data class MainDataBundle(
    val nickname: String,
    val presentations: List<MainDataWithPracticeRecords>,
)
