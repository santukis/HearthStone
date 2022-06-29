package com.santukis.viewmodels.mappers

import com.santukis.viewmodels.entities.UiState

fun Throwable.toErrorMessage(): String = message.orEmpty()

fun Throwable.toUiState(uiState: UiState): UiState =
    uiState.copy(
        isLoading = false,
        errorMessage = toErrorMessage()
    )