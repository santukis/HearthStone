package com.santukis.hearthstone.core.components

import androidx.compose.runtime.*
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

@Composable
fun ExpandableContainer(
    modifier: Modifier = Modifier,
    expandableState: ExpandableState = rememberExpandableState(),
    headerContent: LazyListScope.() -> Unit,
    expandedContent: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.Center
    ) {
        headerContent()

        if (expandableState.isExpanded) {
            expandedContent()
        }
    }
}

@Composable
fun rememberExpandableState(
    initialValue: ExpandedValue = ExpandedValue.Collapsed
): ExpandableState {
    return rememberSaveable(saver = ExpandableState.Saver()) {
        ExpandableState(initialValue)
    }
}

enum class ExpandedValue {
    Collapsed,
    Expanded
}

@Suppress("NotCloseable")
@Stable
class ExpandableState(
    initialValue: ExpandedValue
) {

    val isExpanded: Boolean
        get() = currentValue == ExpandedValue.Expanded

    val isCollapsed: Boolean
        get() = currentValue == ExpandedValue.Collapsed


    private var currentValue: ExpandedValue by mutableStateOf(initialValue)
        private set

    fun expand() {
        currentValue = ExpandedValue.Expanded
    }

    fun collapse() {
        currentValue = ExpandedValue.Collapsed
    }

    fun toggle() {
        if (isExpanded) {
            collapse()

        } else {
            expand()
        }
    }

    companion object {
        fun Saver() =
            Saver<ExpandableState, ExpandedValue>(
                save = { it.currentValue },
                restore = { ExpandableState(it) }
            )
    }
}