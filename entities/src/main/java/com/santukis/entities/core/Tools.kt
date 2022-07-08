package com.santukis.entities.core

fun String.takeIfNotEmpty(): String? = takeIf { it.isNotEmpty() }

fun Int?.takeIfNotDefault(default: Int = -1): Int? = this?.let {
    if (this != default) {
        this
    } else {
        null
    }
}

fun Int?.orDefault(default: Int = -1): Int = this ?: default

fun Long?.orDefault(default: Long = -1L): Long = this ?: default

fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
    filterValues { it != null } as Map<K, V>
