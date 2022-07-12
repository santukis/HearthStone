package com.santukis.hearthstone.collection.components

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import com.santukis.entities.hearthstone.CardType
import com.santukis.entities.hearthstone.SpellSchool
import com.santukis.hearthstone.R
import com.santukis.hearthstone.core.components.ExpandableContainer
import com.santukis.hearthstone.core.components.ExpandableState
import com.santukis.hearthstone.core.components.rememberExpandableState
import com.santukis.viewmodels.entities.*
import com.santukis.viewmodels.entities.CardFilterState.Companion.CARD_SET
import com.santukis.viewmodels.entities.CardFilterState.Companion.CARD_TYPE
import com.santukis.viewmodels.entities.CardFilterState.Companion.SPELL_SCHOOL

@Composable
fun CardFilters(
    cardFilterState: CardFilterState,
    modifier: Modifier = Modifier,
    onUiEvent: (UiEvent) -> Unit = {},
    onCloseFiltersClick: () -> Unit = {}
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            CardFiltersHeader(
                onCloseFiltersClick = onCloseFiltersClick
            )
        }

        cardFilterState.cardFilters.forEach { entry ->
            cardFilterRowFactory(
                filter = entry,
                cardFilterState = cardFilterState,
                onUiEvent = onUiEvent
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
    modifier: Modifier = Modifier,
    expandableState: ExpandableState = rememberExpandableState(),
    onClearFilterClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
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
                .clickable { expandableState.toggle() }
        )
    }
}

fun LazyListScope.cardFilterRowFactory(
    filter: Map.Entry<Int, List<CardFilter<*>>>,
    cardFilterState: CardFilterState,
    onUiEvent: (UiEvent) -> Unit = {}
) {
    when (filter.key) {
        CARD_TYPE -> defaultExpandableRow(
            filter = filter as Map.Entry<Int, List<CardFilter<CardType>>>,
            headerName = R.string.card_types,
            cardFilterState = cardFilterState,
            onUiEvent = onUiEvent,
            onClearFilterClick = { onUiEvent(OnFilterRemoved(CARD_TYPE)) }
        )
        CARD_SET -> defaultExpandableRow(
            filter = filter as Map.Entry<Int, List<CardFilter<CardSet>>>,
            headerName = R.string.card_set,
            cardFilterState = cardFilterState,
            onUiEvent = onUiEvent,
            onClearFilterClick = { onUiEvent(OnFilterRemoved(CARD_SET)) }
        )
        SPELL_SCHOOL -> defaultExpandableRow(
            filter = filter as Map.Entry<Int, List<CardFilter<SpellSchool>>>,
            headerName = R.string.spell_school,
            cardFilterState = cardFilterState,
            onUiEvent = onUiEvent,
            onClearFilterClick = { onUiEvent(OnFilterRemoved(SPELL_SCHOOL)) }
        )
    }
}

private fun <FilterType> LazyListScope.defaultExpandableRow(
    filter: Map.Entry<Int, List<CardFilter<FilterType>>>,
    @StringRes headerName: Int,
    cardFilterState: CardFilterState,
    onUiEvent: (UiEvent) -> Unit = {},
    onClearFilterClick: () -> Unit = {}
) {

    item {
        val expandableState = rememberExpandableState()

        ExpandableContainer(
            expandableState = expandableState,
            headerContent = {
                FilterHeader(
                    headerName = headerName,
                    expandableState = expandableState,
                    onClearFilterClick = onClearFilterClick,
                )
                Divider()
            },
            expandedContent = {
                filter.value.forEach { filterItem ->
                    Text(
                        text = filterItem.getName(),
                        modifier = Modifier
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                            .clickable {
                                onUiEvent(OnFilterSelected(filter.key, filterItem))
                            },
                        color = cardFilterState.getFilterColor(filter.key, filterItem),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        )
    }
}