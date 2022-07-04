package com.santukis.hearthstone.core.components

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
val BottomSheetScaffoldState.currentBottomSheetFraction: Float
    get() {
        val fraction = bottomSheetState.progress.fraction
        val targetValue = bottomSheetState.targetValue
        val currentValue = bottomSheetState.currentValue

        return when {
            currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Collapsed -> 0f
            currentValue == BottomSheetValue.Expanded && targetValue == BottomSheetValue.Expanded -> 1f
            currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Expanded -> fraction
            else -> 1f - fraction
        }
    }

@Composable
fun Int.transformToDp(): Dp {
    return if (this == 0) {
        BottomSheetScaffoldDefaults.SheetPeekHeight

    } else {
        (with(LocalDensity.current) { (this@transformToDp + 80.dp.toPx()) / density }).dp
    }
}