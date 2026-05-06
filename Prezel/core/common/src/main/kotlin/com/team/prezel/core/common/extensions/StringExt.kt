package com.team.prezel.core.common.extensions

fun String.toSnakeCase(): String =
    fold(StringBuilder()) { acc, c ->
        if (c.isUpperCase() && acc.isNotEmpty()) acc.append('_')
        acc.append(c.uppercaseChar())
    }.toString()
