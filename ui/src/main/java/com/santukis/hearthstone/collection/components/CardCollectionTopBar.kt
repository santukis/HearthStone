package com.santukis.hearthstone.collection.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.santukis.hearthstone.core.animations.alpha
import com.santukis.viewmodels.entities.CardFilterState
import com.santukis.viewmodels.entities.FilterUI
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardCollectionTopBar(
    scaffoldState: BottomSheetScaffoldState,
    cardFilterState: CardFilterState,
    modifier: Modifier = Modifier,
    onSelectedCardClassClick: () -> Unit = {},
    onRemoveFilterClick: (Int) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val constraints = ConstraintSet {
            val startButton = createRefFor("startButton")
            val endButton = createRefFor("endButton")
            val activeFilters = createRefFor("activeFilters")

            constrain(startButton) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }

            constrain(endButton) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }

            constrain(activeFilters) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(startButton.end, margin = 2.dp)
                end.linkTo(endButton.start, margin = 2.dp)
                width = Dimension.preferredWrapContent
            }
        }

        ConstraintLayout(
            constraintSet = constraints,
            modifier = modifier.fillMaxWidth()
        ) {

            TopBarButton(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                scaffoldState = scaffoldState,
                modifier = Modifier.layoutId("startButton")
            ) {

                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "menu",
                )

            }

            TopBarButton(
                onClick = {
                    onSelectedCardClassClick()
                },
                scaffoldState = scaffoldState,
                modifier = Modifier.layoutId("endButton")
            ) {

                Image(
                    painter = painterResource(id = cardFilterState.getSelectedCardClassDrawable()),
                    contentDescription = "",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }

            LazyRow(
                modifier = Modifier
                    .graphicsLayer {
                        alpha(scaffoldState)
                    }
                    .layoutId("activeFilters")
            ) {

                items(cardFilterState.getActiveFilters()) { filter ->
                    ActiveFilter(
                        filter = filter,
                        onRemoveFilterClick = {
                            onRemoveFilterClick(filter.key)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBarButton(
    onClick: () -> Unit,
    scaffoldState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .size(48.dp)
            .graphicsLayer {
                alpha(scaffoldState)
            },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        )
    ) {
        content()
    }
}