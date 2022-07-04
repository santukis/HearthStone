package com.santukis.hearthstone.collection.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.santukis.entities.hearthstone.CardClass
import com.santukis.entities.hearthstone.Rarity
import com.santukis.hearthstone.core.animations.zoom
import com.santukis.viewmodels.hearthstone.HearthstoneViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import com.santukis.hearthstone.core.components.*
import com.santukis.hearthstone.theme.WhiteTransparent
import com.santukis.viewmodels.entities.*


@Composable
fun CardCollectionScreen(
    viewModel: HearthstoneViewModel
) {
    val uiState = viewModel.uiState
    val collectionState = viewModel.cardCollectionState
    val cardDetailState = viewModel.cardDetailState
    val cardFilterState = viewModel.cardFilterState

    val onCardSelected: (Int) -> Unit = { cardIndex ->
        viewModel.onCardSelected(cardIndex)
    }

    val onEndReached: () -> Unit = {
        viewModel.onEndReached()
    }

    val onFavouriteClick: () -> Unit = {
        viewModel.onFavouriteClick()
    }

    val onCardClassSelected: (CardClass) -> Unit = { cardClass ->
        viewModel.onCardClassSelected(cardClass)
    }

    val onSelectedCardClassClick: () -> Unit = {
        viewModel.onSelectedCardClassClick()
    }

    val onManaCostSelected: (Int) -> Unit = { cost ->
        viewModel.onManaCostSelected(cost)
    }

    val onCardRaritySelected: (Rarity?) -> Unit = { rarity ->
        viewModel.onCardRaritySelected(rarity)
    }

    val onRemoveFilterClick: (Int) -> Unit = { filter ->
        viewModel.onRemoveFilterClick(filter)
    }

    CardCollectionContent(
        uiState = uiState,
        cardCollectionState = collectionState,
        cardDetailState = cardDetailState,
        cardFilterState = cardFilterState,
        onCardSelected = onCardSelected,
        onFavouriteClick = onFavouriteClick,
        onCardClassSelected = onCardClassSelected,
        onSelectedCardClassClick = onSelectedCardClassClick,
        onManaCostSelected = onManaCostSelected,
        onCardRaritySelected = onCardRaritySelected,
        onRemoveFilterClick = onRemoveFilterClick,
        onEndReached = onEndReached
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardCollectionContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    cardCollectionState: CardCollectionState,
    cardDetailState: CardDetailState,
    cardFilterState: CardFilterState,
    onCardSelected: (Int) -> Unit = {},
    onFavouriteClick: () -> Unit = {},
    onCardClassSelected: (CardClass) -> Unit = {},
    onSelectedCardClassClick: () -> Unit = {},
    onManaCostSelected: (Int) -> Unit = {},
    onCardRaritySelected: (Rarity?) -> Unit = {},
    onRemoveFilterClick: (Int) -> Unit = {},
    onEndReached: () -> Unit = {}
) {

    Box(modifier = modifier) {
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed),
            drawerState = rememberDrawerState(DrawerValue.Closed)
        )

        var sheetPeekHeight by remember { mutableStateOf(0) }

        BottomSheetScaffold(
            modifier = modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            topBar = {
                CardCollectionTopBar(
                    scaffoldState = scaffoldState,
                    cardFilterState = cardFilterState,
                    onSelectedCardClassClick = onSelectedCardClassClick,
                    onRemoveFilterClick = onRemoveFilterClick
                )
            },
            sheetContent = {
                CardDetailContent(
                    scaffoldState = scaffoldState,
                    cardDetailState = cardDetailState,
                    onFavouriteClick = onFavouriteClick,
                    onHeaderHeightChange = { sheetPeekHeight = it }
                )
            },
            sheetPeekHeight = sheetPeekHeight.transformToDp(),
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Transparent,
            drawerContent = {
                CardFilters(
                    cardFilterState = cardFilterState,
                    onManaCostSelected = onManaCostSelected,
                    onCardRaritySelected = onCardRaritySelected,
                    onClearFilterClick = onRemoveFilterClick
                )
            },
            drawerElevation = 20.dp,
            drawerBackgroundColor = WhiteTransparent
        ) {

            CardCollection(
                cardCollectionState = cardCollectionState,
                onCardSelected = onCardSelected,
                onEndReached = onEndReached
            )
        }

        CardClassCollection(
            scaffoldState = scaffoldState,
            cardFilterState = cardFilterState,
            onCardClassSelected = onCardClassSelected
        )
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun CardCollection(
    cardCollectionState: CardCollectionState,
    modifier: Modifier = Modifier,
    onCardSelected: (Int) -> Unit = {},
    onEndReached: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnEndReached(
        onEndReached = {
            onEndReached()
        }
    )

    listState.ScrollListener(
        onScrollStateChanged = { scrolling ->
            onCardSelected(if (scrolling) -1 else listState.firstVisibleItemIndex)
        }
    )
    LazyRow(
        state = listState,
        flingBehavior = rememberSnapperFlingBehavior(lazyListState = listState),
        modifier = modifier.fillMaxSize()
    ) {
        items(cardCollectionState.cards.size) { index ->

            val card = cardCollectionState.cards[index]

            AsyncImage(
                model = card.images.image,
                contentDescription = "",
                modifier = Modifier
                    .fillParentMaxWidth()
                    .fillParentMaxHeight(0.65f)
                    .padding(top = 64.dp)
                    .graphicsLayer {
                        zoom(
                            listState = listState,
                            page = index
                        )
                    }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun CardClassCollection(
    scaffoldState: BottomSheetScaffoldState,
    cardFilterState: CardFilterState,
    modifier: Modifier = Modifier,
    onCardClassSelected: (CardClass) -> Unit = {}
) {
    AnimatedVisibility(
        visible = scaffoldState.drawerState.isClosed
                && scaffoldState.bottomSheetState.isCollapsed
                && cardFilterState.shouldShowCardClassList,
        enter = fadeIn() + scaleIn(
            transformOrigin = TransformOrigin(
                pivotFractionY = 0.15f,
                pivotFractionX = 0.95f
            )
        ),
        exit = fadeOut() + scaleOut(
            transformOrigin = TransformOrigin(
                pivotFractionY = 0.15f,
                pivotFractionX = 0.95f
            )
        )
    ) {

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = 64.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            backgroundColor = WhiteTransparent
        ) {

            LazyRow {
                items(cardFilterState.getCardClasses()) { cardClass ->
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = cardClass.getDrawable()),
                            contentDescription = "",
                            modifier = Modifier
                                .size(51.dp)
                                .clip(CircleShape)
                                .clickable {
                                    onCardClassSelected(cardClass)
                                }
                        )

                        Text(
                            modifier = Modifier
                                .widthIn(max = 50.dp),
                            text = cardClass.identity.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardCollectionContentPreview() {
    CardCollectionContent(
        uiState = UiState(),
        cardCollectionState = CardCollectionState(),
        cardDetailState = CardDetailState(),
        cardFilterState = CardFilterState()
    )
}