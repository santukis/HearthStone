package com.santukis.viewmodels.mappers

fun Throwable.toErrorMessage(): String = message.orEmpty()