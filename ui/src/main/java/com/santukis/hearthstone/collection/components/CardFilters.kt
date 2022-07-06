package com.santukis.hearthstone.collection.components

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.santukis.entities.hearthstone.CardSet
import com.santukis.hearthstone.R
import com.santukis.hearthstone.core.components.ExpandableContainer
import com.santukis.hearthstone.core.components.ExpandableState
import com.santukis.hearthstone.core.components.rememberExpandableState
import com.santukis.viewmodels.entities.CardFilter
import com.santukis.viewmodels.entities.CardFilterState
import com.santukis.viewmodels.entities.CardFilterState.Companion.CARD_SET

@Composable
fun CardFilters(
    cardFilterState: CardFilterState,
    modifier: Modifier = Modifier,
    onFilterSelected: (Int, CardFilter<*>) -> Unit = { _, _ ->},
    onClearFilterClick: (Int) -> Unit = {},
    onCloseFiltersClick: () -> Unit = {}
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            CardFiltersHeader(
                onCloseFiltersClick = onCloseFiltersClick
            )
        }

        cardFilterState.cardFilters.forEach { entry ->
            createCardFilterRow(
                filter = entry,
                cardFilterState = cardFilterState,
                onFilterSelected = onFilterSelected,
                onClearFilterClick = onClearFilterClick
            )
        }
    }
}

@Composable
fun CardFiltersHeader(
    modifier: Modifier = Modifier,
    onCloseFiltersClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Button(
            onClick = { onCloseFiltersClick() },
            shape = CircleShape,
            modifier = modifier
                .size(48.dp)
                .align(Alignment.TopStart),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close"
            )
        }

        Text(
            modifier = modifier
                .align(Alignment.Center),
            text = stringResource(id = R.string.filters),
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun FilterHeader(
    @StringRes headerName: Int,
    expandableState: ExpandableState,
    modifier: Modifier = Modifier,
    onClearFilterClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(id = headerName),
            style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.weight(fill = true, weight = 1f))

        Text(
            text = stringResource(id = R.string.clear),
            modifier = Modifier
                .padding(all = 8.dp)
                .clickable { onClearFilterClick() },
            style = MaterialTheme.typography.body2
        )

        Icon(
            imageVector = if (expandableState.isExpanded) {
                Icons.Filled.ArrowDropUp
            } else {
                Icons.Filled.ArrowDropDown
            },
            contentDescription = "Expand/Contract icon",
            modifier = Modifier
                .clickable {
                    if (expandableState.isExpanded) {
                        expandableState.collapse()

                    } else {
                        expandableState.expand()
                    }
                }
        )
    }
}

fun LazyListScope.createCardFilterRow(
    filter: Map.Entry<Int, List<CardFilter<*>>>,
    cardFilterState: CardFilterState,
    onFilterSelected: (Int, CardFilter<*>) -> Unit = { _, _ -> },
    onClearFilterClick: (Int) -> Unit = {}
) {
    when (filter.key) {
        CARD_SET -> createCardSetFilterRow(
            filterList = filter.value.filterIsInstance<CardFilter<CardSet>>(),
            cardFilterState = cardFilterState,
            onFilterSelected = onFilterSelected,
            onClearFilterClick = { onClearFilterClick(CARD_SET) }
        )
    }
}

private fun LazyListScope.createCardSetFilterRow(
    filterList: List<CardFilter<CardSet>>,
    cardFilterState: CardFilterState,
    onFilterSelected: (Int, CardFilter<*>) -> Unit = { _, _ -> },
    onClearFilterClick: () -> Unit = {}
) {

    item {
        val expandableState = rememberExpandableState()

        ExpandableContainer(
            expandableState = expandableState,
            headerContent = {
                FilterHeader(
                    headerName = R.string.card_set,
                    expandableState = expandableState,
                    onClearFilterClick = onClearFilterClick,
                )
                Divider()
            },
            expandedContent = {
                filterList.forEach { filter ->
                    Text(
                        text = filter.getName(),
                        modifier = Modifier
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                            .clickable {
                                onFilterSelected(CARD_SET, filter)
                            },
                        color = cardFilterState.getFilterColor(CARD_SET, filter),
                        style = MaterialTheme.typography.subtitle1
                    )

                }
            }
        )
    }
}