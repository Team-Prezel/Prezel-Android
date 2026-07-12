package com.team.prezel.core.model.presentation

data class Curation(
    val guideMessage: String,
    val materialType: String,
    val title: String,
    val sourceChannel: String,
    val linkUrl: String,
    val imageUrl: String,
)
