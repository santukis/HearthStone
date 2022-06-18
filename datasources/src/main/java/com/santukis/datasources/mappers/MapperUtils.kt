package com.santukis.datasources.mappers

fun String.takeIfNotEmpty(): String? = takeIf { it.isNotEmpty() }

fun Int?.orDefault(default: Int = -1): Int = this ?: default