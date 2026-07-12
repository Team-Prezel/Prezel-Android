package com.team.prezel.core.model.terms

data class Term(
    val termsId: Long,
    val title: String,
    val summary: String,
    val content: String,
    val isRequired: Boolean,
    val version: String,
)
