package com.santukis.hearthstone.collection.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.santukis.hearthstone.R
import com.santukis.hearthstone.core.animations.zoom
import com.santukis.viewmodels.entities.CardCollectionState
import com.santukis.viewmodels.hearthstone.HearthstoneViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import com.santukis.hearthstone.core.components.*
import com.santukis.hearthstone.theme.WhiteTransparent
import com.santukis.viewmodels.entities.CardDetailState
import com.santukis.viewmodels.entities.UiState
import kotlinx.coroutines.launch


@Composable
fun CardCollection(
    viewModel: HearthstoneViewModel
) {
    val uiState = viewModel.uiState
    val collectionUiState = viewModel.cardCollectionState
    val cardDetailUiState = viewModel.cardDetailState

    val onCardSelected: (Int) -> Unit = { cardIndex ->
        viewModel.onCardSelected(cardIndex)
    }

    val onEndReached: (Int) -> Unit = { itemCount ->
        viewModel.loadMoreItems(itemCount)
    }

    val onFavouriteClick: () -> Unit = {
        viewModel.updateCardFavourite()
    }

    CardCollectionContent(
        uiState = uiState,
        cardCollectionState = collectionUiState,
        cardDetailState = cardDetailUiState,
        onCardSelected = onCardSelected,
        onFavouriteClick = onFavouriteClick,
        onEndReached = onEndReached
    )
}

@OptIn(ExperimentalSnapperApi::class, ExperimentalMaterialApi::class)
@Composable
fun CardCollectionContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    cardCollectionState: CardCollectionState,
    cardDetailState: CardDetailState,
    onCardSelected: (Int) -> Unit = {},
    onFavouriteClick: () -> Unit = {},
    onEndReached: (Int) -> Unit = {}
) {

    Box(modifier = modifier) {
        val scope = rememberCoroutineScope()

        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed),
            drawerState = rememberDrawerState(DrawerValue.Closed)
        )

        BottomSheetScaffold(
            modifier = modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            sheetContent = {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(
                            onClick = { /*TODO onAddClick */ },
                            shape = CircleShape,
                            modifier = Modifier
                                .size(40.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "add"
                            )
                        }

                        Divider(
                            modifier = Modifier
                                .width(75.dp)
                                .padding(16.dp)
                                .clickable {
                                    scope.launch {
                                        if (scaffoldState.bottomSheetState.isCollapsed) {
                                            scaffoldState.bottomSheetState.expand()

                                        } else {
                                            scaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                },
                            color = Color.White,
                            thickness = 2.dp
                        )

                        Button(
                            onClick = {
                                onFavouriteClick()
                            },
                            shape = CircleShape,
                            modifier = Modifier
                                .size(40.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {

                            val icon = if (cardDetailState.card?.isFavourite == true) {
                                Icons.Filled.Favorite

                            } else {
                                Icons.Filled.FavoriteBorder
                            }

                            Icon(
                                imageVector = icon,
                                contentDescription = "favorite",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .background(WhiteTransparent)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                    ) {

                        Text(
                            text = cardDetailState.card?.identity?.name.orEmpty(),
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 24.sp,
                                shadow = Shadow(
                                    color = Color.Gray,
                                    offset = Offset(x = 0f, y = 0f),
                                    blurRadius = 5f
                                )
                            ),
                            fontWeight = FontWeight.Bold,
                        )

                        if (cardDetailState.relatedCards.isNotEmpty()) {
                            Text(
                                text = stringResource(id = R.string.related_cards),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                textAlign = TextAlign.Start,
                                style = TextStyle(
                                    fontSize = 16.sp
                                ),
                                fontWeight = FontWeight.Bold,
                            )

                            LazyRow {
                                items(cardDetailState.relatedCards.size) { index ->
                                    val card = cardDetailState.relatedCards[index]

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(350.dp),
                                    ) {
                                        AsyncImage(
                                            model = card.images.image,
                                            contentDescription = "",
                                            modifier = Modifier.fillParentMaxWidth(fraction = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            sheetPeekHeight = 125.dp,
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Transparent,
            drawerContent = {
                Box {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = stringResource(id = R.string.filters),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ) {

            val listState = rememberLazyListState()

            listState.OnEndReached(
                onEndReached = {
                    onEndReached(it)
                }
            )

            listState.ScrollListener(
                onScrollStateChanged = { scrolling ->
                    onCardSelected(if (scrolling) -1 else listState.firstVisibleItemIndex)
                }
            )

            LazyRow(
                state = listState,
                flingBehavior = rememberSnapperFlingBehavior(lazyListState = listState)
            ) {
                items(cardCollectionState.cards.size) { index ->

                    val card = cardCollectionState.cards[index]

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(500.dp),
                    ) {

                        AsyncImage(
                            model = card.images.image,
                            contentDescription = "",
                            modifier = Modifier
                                .fillParentMaxWidth()
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
        }

        Button(
            onClick = {
                scope.launch {
                    if (scaffoldState.drawerState.isOpen) {
                        scaffoldState.drawerState.close()

                    } else {
                        scaffoldState.drawerState.open()
                    }
                }
            },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .size(40.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp)
        ) {

            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "menu",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardCollectionContentPreview() {
    CardCollectionContent(
        uiState = UiState(),
        cardCollectionState = CardCollectionState(),
        cardDetailState = CardDetailState()
    )
}