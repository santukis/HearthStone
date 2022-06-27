package com.santukis.hearthstone.collection.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.santukis.hearthstone.core.animations.zoom
import com.santukis.viewmodels.entities.CardCollectionState
import com.santukis.viewmodels.hearthstone.HearthstoneViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import com.santukis.hearthstone.core.components.*
import com.santukis.hearthstone.theme.WhiteTransparent
import com.santukis.viewmodels.entities.CardDetailState


@Composable
fun CardCollection(
    viewModel: HearthstoneViewModel
) {
    val collectionUiState = viewModel.cardCollectionState
    val cardDetailUiState = viewModel.cardDetailState

    val onCardSelected: (Int) -> Unit = { card ->
        viewModel.onCardSelected(card)
    }

    val onEndReached: (Int) -> Unit = { itemCount ->
        viewModel.searchCards(itemCount)
    }

    CardCollectionContent(
        cardCollectionState = collectionUiState,
        cardDetailState = cardDetailUiState,
        onCardSelected = onCardSelected,
        onEndReached = onEndReached
    )
}

@OptIn(ExperimentalSnapperApi::class, ExperimentalMaterialApi::class)
@Composable
fun CardCollectionContent(
    modifier: Modifier = Modifier,
    cardCollectionState: CardCollectionState,
    cardDetailState: CardDetailState,
    onCardSelected: (Int) -> Unit = {},
    onEndReached: (Int) -> Unit = {}
) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        sheetContent = {
            SheetContent {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider(
                        modifier = Modifier
                            .width(75.dp)
                            .padding(16.dp)
                            .clickable {
                            },
                        color = Color.Gray,
                        thickness = 2.dp
                    )
                    Text(
                        text = cardDetailState.card?.identity?.name.orEmpty(),
                        modifier = Modifier
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

                    Text(
                        text = "Related Cards",
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
                                    .fillMaxWidth(),
                            ) {

                                AsyncImage(
                                    model = card.images.image,
                                    contentDescription = "",
                                    modifier = Modifier.fillParentMaxWidth(fraction = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 80.dp,
        sheetElevation = 8.dp,
        sheetBackgroundColor = WhiteTransparent
    ) {

        val listState = rememberLazyListState()

        listState.OnEndReached(
            onEndReached = {
                onEndReached(it)
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
                        .fillMaxWidth(),
                ) {

                    Text(
                        text = "$index",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(16.dp)
                    )

                    AsyncImage(
                        model = card.images.image,
                        contentDescription = "",
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .align(Alignment.TopCenter)
                            .graphicsLayer {
                                zoom(
                                    listState = listState,
                                    page = index
                                )
                            }
                    )
                }

                if (listState.isScrollInProgress) {
                    onCardSelected(-1)

                } else {
                    onCardSelected(listState.firstVisibleItemIndex)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardCollectionContentPreview() {
    CardCollectionContent(
        cardCollectionState = CardCollectionState(),
        cardDetailState = CardDetailState()
    )
}