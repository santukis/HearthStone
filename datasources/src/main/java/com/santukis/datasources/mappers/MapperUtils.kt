package com.santukis.datasources.mappers

fun String.takeIfNotEmpty(): String? = takeIf { it.isNotEmpty() }

fun Int?.takeIfNotDefault(default: Int = -1): Int? = this?.let {
    if (this != default) {
        this
    } else {
        null
    }
}

fun Int?.orDefault(default: Int = -1): Int = this ?: default

inline fun <reified Item> Item?.orDefault(default: Item = Item::class.java.newInstance()): Item = this ?: default