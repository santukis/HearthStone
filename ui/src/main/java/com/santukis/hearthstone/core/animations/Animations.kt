package com.santukis.hearthstone.core.animations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.util.lerp

fun GraphicsLayerScope.zoom(
    listState: LazyListState,
    page: Int,
    from: Float = 0.75f,
    toInclusive: Float = 1.0f
) {

    val isCurrentPage = listState.firstVisibleItemIndex != page

    val offset = listState.firstVisibleItemScrollOffset / listState.layoutInfo.viewportEndOffset.toFloat()

    val pageOffset = if (isCurrentPage) {
        1 - offset
    } else {
        offset
    }

    lerp(
        start = from,
        stop = toInclusive,
        fraction = 1 - pageOffset.coerceIn(0f, 1f)
    ).also { scale ->
        scaleX = scale
        scaleY = scale
    }
}