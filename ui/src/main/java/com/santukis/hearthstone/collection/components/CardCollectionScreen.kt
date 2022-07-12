package com.santukis.hearthstone.collection.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.santukis.hearthstone.core.animations.zoom
import com.santukis.viewmodels.hearthstone.HearthstoneViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import com.santukis.hearthstone.core.components.*
import com.santukis.hearthstone.theme.WhiteTransparent
import com.santukis.viewmodels.entities.*
import com.santukis.viewmodels.entities.CardFilterState.Companion.CARD_CLASS
import kotlinx.coroutines.launch


@Composable
fun CardCollectionScreen(
    viewModel: HearthstoneViewModel
) {
    val uiState = viewModel.uiState
    val collectionState = viewModel.cardCollectionState
    val cardDetailState = viewModel.cardDetailState
    val cardFilterState = viewModel.cardFilterState

    val onUiEvent: (UiEvent) -> Unit = { uiEvent ->
        viewModel.onUiEvent(uiEvent)
    }

    CardCollectionContent(
        uiState = uiState,
        cardCollectionState = collectionState,
        cardDetailState = cardDetailState,
        cardFilterState = cardFilterState,
        onUiEvent = onUiEvent
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
    onUiEvent: (UiEvent) -> Unit = {}
) {

    val coroutineScope = rememberCoroutineScope()

    val cardClassListVisibleState = rememberVisibleState(initialValue = VisibleValue.Invisible)

    Box(modifier = modifier) {
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed),
            drawerState = rememberDrawerState(DrawerValue.Closed),
            snackbarHostState = remember { SnackbarHostState() }
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
                    coroutineScope = coroutineScope,
                    visibleState = cardClassListVisibleState,
                    onUiEvent = onUiEvent
                )
            },
            sheetContent = {
                CardDetailContent(
                    scaffoldState = scaffoldState,
                    cardDetailState = cardDetailState,
                    coroutineScope = coroutineScope,
                    onUiEvent = onUiEvent,
                    onHeaderHeightChange = { sheetPeekHeight = it }
                )
            },
            sheetPeekHeight = sheetPeekHeight.transformToDp(),
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Transparent,
            drawerContent = {
                CardFilters(
                    cardFilterState = cardFilterState,
                    onUiEvent = onUiEvent,
                    onCloseFiltersClick = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                )
            },
            drawerElevation = 20.dp,
            drawerBackgroundColor = WhiteTransparent
        ) {

            CardCollection(
                cardCollectionState = cardCollectionState,
                onUiEvent = onUiEvent
            )
        }

        CardClassCollection(
            scaffoldState = scaffoldState,
            cardFilterState = cardFilterState,
            visibleState = cardClassListVisibleState,
            onUiEvent = onUiEvent
        )
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun CardCollection(
    cardCollectionState: CardCollectionState,
    modifier: Modifier = Modifier,
    onUiEvent: (UiEvent) -> Unit = {}
) {
    val listState = rememberLazyListState()

    var visibleImageIndex by remember { mutableStateOf(0) }

    listState.OnEndReached(
        onEndReached = {
            onUiEvent(OnEndReached(it))
        }
    )

    listState.ScrollListener(
        onScrollStateChanged = { scrolling ->
            val cardIndex = if (scrolling) -1 else listState.firstVisibleItemIndex
            onUiEvent(OnCardSelected(cardIndex))
        }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                bottom = 175.dp
            )
    ) {

        LazyRow(
            state = listState,
            flingBehavior = rememberSnapperFlingBehavior(lazyListState = listState),
            modifier = modifier
                .fillMaxSize()
        ) {
            items(cardCollectionState.cards.size) { index ->
                val card = cardCollectionState.cards[index]

                val cardImageListState = rememberLazyListState()

                LazyColumn(
                    state = cardImageListState,
                    modifier = Modifier
                        .fillParentMaxSize()
                        .graphicsLayer {
                            zoom(
                                listState = listState,
                                page = index
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround

                ) {
                    items(card.images.asList()) { image ->
                        AsyncImage(
                            model = image,
                            contentDescription = "",
                            modifier = Modifier
                                .fillParentMaxSize(),
                        )
                    }

                    visibleImageIndex = cardImageListState.firstVisibleItemIndex
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(24.dp)
                .padding(8.dp)
        ) {
            cardCollectionState.cards.getOrNull(listState.firstVisibleItemIndex)?.let { card ->
                items(card.images.asList().size) { index ->
                    if (visibleImageIndex == index) {
                        Icon(
                            imageVector = Icons.Filled.Circle,
                            contentDescription = "selected Image",
                            modifier = Modifier
                                .width(12.dp),
                            tint = MaterialTheme.colors.primary
                        )

                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Circle,
                            contentDescription = "unselected Image",
                            modifier = Modifier
                                .width(12.dp),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun CardClassCollection(
    scaffoldState: BottomSheetScaffoldState,
    cardFilterState: CardFilterState,
    modifier: Modifier = Modifier,
    visibleState: VisibleState = rememberVisibleState(),
    onUiEvent: (UiEvent) -> Unit = {}
) {

    AnimatedVisibility(
        visible = scaffoldState.drawerState.isClosed
                && scaffoldState.bottomSheetState.isCollapsed
                && visibleState.isVisible,
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
                                    visibleState.toggle()
                                    onUiEvent(OnFilterSelected(CARD_CLASS, cardClass.asCardFilter()))
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