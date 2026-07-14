package com.team.prezel.core.model.presentation

data class Curation(
    val guideMessage: String,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val materialType: String,
    val title: String,
    val sourceChannel: String,
    val linkUrl: String,
    val imageUrl: String,
)
