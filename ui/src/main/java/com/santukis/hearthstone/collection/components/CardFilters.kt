package com.santukis.hearthstone.collection.components

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.santukis.entities.hearthstone.Rarity
import com.santukis.hearthstone.R
import com.santukis.hearthstone.core.components.AutoSizeText
import com.santukis.viewmodels.entities.CardFilterState
import com.santukis.viewmodels.entities.getDrawable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardFilters(
    cardFilterState: CardFilterState,
    modifier: Modifier = Modifier,
    onManaCostSelected: (Int) -> Unit = {},
    onCardRaritySelected: (Rarity?) -> Unit = {},
    onClearFilterClick: (Int) -> Unit = {},
    onCloseFiltersClick: () -> Unit = {}
) {

    Column(
        modifier = modifier
            .padding(8.dp)
    ) {

        Box {

            Button(
                onClick = { onCloseFiltersClick() },
                shape = CircleShape,
                modifier = modifier
                    .size(48.dp)
                    .align(Alignment.TopStart),
                contentPadding = PaddingValues(0.dp),
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
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                text = stringResource(id = R.string.filters),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
        }

        FilterHeader(
            headerName = R.string.mana_cost,
            onClearFilterClick = {
                onClearFilterClick(com.santukis.viewmodels.R.string.mana_cost_filter)
            }
        )

        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 64.dp),
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    start = 8.dp
                )
        ) {

            items(11) { cost ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onManaCostSelected(cost)
                        }
                ) {
                    Image(
                        painter = painterResource(com.santukis.viewmodels.R.drawable.mana),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(
                                top = 8.dp
                            )
                            .size(48.dp)
                    )

                    AutoSizeText(
                        text = cost.toString(),
                        color = cardFilterState.getManaCostColor(cost),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(
                                start = 12.dp
                            )
                            .width(32.dp),
                        textStyle = MaterialTheme.typography.h3
                    )
                }
            }
        }

        FilterHeader(
            headerName = R.string.rarity,
            onClearFilterClick = {
                onClearFilterClick(com.santukis.viewmodels.R.string.rarity_filter)
            }
        )

        LazyVerticalGrid(
            cells = GridCells.Fixed(count = 2),
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    start = 8.dp
                )
        ) {
            items(cardFilterState.getRarities()) { rarity ->
                Row(
                    modifier = Modifier
                        .clickable {
                            onCardRaritySelected(rarity)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(rarity.getDrawable()),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(32.dp)
                    )

                    Text(
                        text = rarity.identity.name,
                        color = cardFilterState.getRarityNameColor(rarity),
                        modifier = Modifier
                            .padding(start = 12.dp),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    }
}

@Composable
fun FilterHeader(
    @StringRes headerName: Int,
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

        Text(
            text = stringResource(id = R.string.clear),
            modifier = Modifier
                .clickable { onClearFilterClick() },
            style = MaterialTheme.typography.body2
        )
    }
}