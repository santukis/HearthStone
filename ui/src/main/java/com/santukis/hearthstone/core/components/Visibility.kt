package com.santukis.hearthstone.core.components

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberVisibleState(
    initialValue: VisibleValue = VisibleValue.Visible
): VisibleState {
    return rememberSaveable(saver = VisibleState.Saver()) {
        VisibleState(initialValue)
    }
}

enum class VisibleValue {
    Visible,
    Invisible
}

@Suppress("NotCloseable")
@Stable
class VisibleState(
    initialValue: VisibleValue
) {

    val isVisible: Boolean
        get() = currentValue == VisibleValue.Visible

    val isInvisible: Boolean
        get() = currentValue == VisibleValue.Invisible


    private var currentValue: VisibleValue by mutableStateOf(initialValue)
        private set

    fun show() {
        currentValue = VisibleValue.Visible
    }

    fun hide() {
        currentValue = VisibleValue.Invisible
    }

    fun toggle() {
        if (isVisible) {
            hide()

        } else {
            show()
        }
    }

    companion object {
        fun Saver() =
            Saver<VisibleState, VisibleValue>(
                save = { it.currentValue },
                restore = { VisibleState(it) }
            )
    }
}