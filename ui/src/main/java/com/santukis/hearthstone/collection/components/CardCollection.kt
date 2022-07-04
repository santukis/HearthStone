package com.santukis.hearthstone.collection.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.santukis.entities.hearthstone.CardClass
import com.santukis.hearthstone.R
import com.santukis.hearthstone.core.animations.zoom
import com.santukis.viewmodels.hearthstone.HearthstoneViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import com.santukis.hearthstone.core.components.*
import com.santukis.hearthstone.theme.WhiteTransparent
import com.santukis.viewmodels.entities.*
import kotlinx.coroutines.launch


@Composable
fun CardCollection(
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
        viewModel.loadMoreItems()
    }

    val onFavouriteClick: () -> Unit = {
        viewModel.updateCardFavourite()
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
        onEndReached = onEndReached
    )
}

@OptIn(ExperimentalSnapperApi::class, ExperimentalMaterialApi::class, ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
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
    onEndReached: () -> Unit = {}
) {

    Box(modifier = modifier) {
        val scope = rememberCoroutineScope()

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
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(48.dp)
                            .graphicsLayer {
                                alpha = 1f - scaffoldState.currentBottomSheetFraction
                            }
                        ,
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

                    LazyRow(
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = 1f - scaffoldState.currentBottomSheetFraction
                            }
                    ) {
                        items(cardFilterState.getActiveFilters()) { filter ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(size = 30.dp))
                                    .background(MaterialTheme.colors.secondary)

                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(
                                        id = filter.key,
                                        filter.value
                                    ),
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            onSelectedCardClassClick()
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(48.dp)
                            .graphicsLayer {
                                alpha = 1f - scaffoldState.currentBottomSheetFraction
                            }
                        ,
                        contentPadding = PaddingValues(0.dp)
                    ) {

                        Image(
                            painter = painterResource(id = cardFilterState.getSelectedCardClassDrawable()),
                            contentDescription = "",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            },
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
                                .size(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "add",
                                tint = MaterialTheme.colors.primary
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
                                .size(48.dp),
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
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {

                        cardDetailState.card?.let { card ->
                            Column(
                                modifier = Modifier
                                    .padding(
                                        top = 8.dp,
                                        start = 16.dp,
                                        end = 16.dp
                                    )
                                    .onGloballyPositioned {
                                        sheetPeekHeight = it.size.height
                                    }
                            ) {
                                Box {
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

                                    Text(
                                        text = cardDetailState.getManaCost(),
                                        color = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(
                                                start = 12.dp
                                            ),
                                        style = MaterialTheme.typography.h3
                                    )

                                    Text(
                                        text = card.identity.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 50.dp,
                                                end = 50.dp
                                            ),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.h4,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Text(
                                    text = card.cardText.flavorText,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Serif,
                                        fontStyle = FontStyle.Italic
                                    )
                                )
                            }


                            Text(
                                text = stringResource(id = R.string.rarity),
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    start = 16.dp
                                ),
                                style = MaterialTheme.typography.h5,
                            )

                            Row(
                                modifier = Modifier.padding(
                                    top = 8.dp,
                                    start = 16.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = card.rarity.identity.name,
                                    style = TextStyle(
                                        fontSize = 16.sp
                                    )
                                )

                                Image(
                                    painter = painterResource(cardDetailState.getRarityDrawable()),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .size(24.dp)
                                )
                            }

                            if (cardDetailState.relatedCards.isNotEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.related_cards),
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        start = 16.dp
                                    ),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.h5,
                                )

                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    items(cardDetailState.relatedCards.size) { index ->
                                        val relatedCard = cardDetailState.relatedCards[index]

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(350.dp),
                                        ) {
                                            AsyncImage(
                                                model = relatedCard.images.image,
                                                contentDescription = "",
                                                modifier = Modifier.fillParentMaxWidth(fraction = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            sheetPeekHeight = if (sheetPeekHeight == 0) {
                BottomSheetScaffoldDefaults.SheetPeekHeight

            } else {
                (with(LocalDensity.current) { (sheetPeekHeight + 80.dp.toPx()) / density }).dp
            },
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Transparent,
            drawerElevation = 20.dp,
            drawerContent = {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = stringResource(id = R.string.filters),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        Text(
                            text = stringResource(id = R.string.mana_cost),
                            style = MaterialTheme.typography.h6,
                        )

                        Text(
                            text = stringResource(id = R.string.clear),
                            modifier = Modifier
                                .clickable { onManaCostSelected(-1) },
                            style = MaterialTheme.typography.body2
                        )
                    }

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
                }
            },
            drawerBackgroundColor = WhiteTransparent
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
                modifier = Modifier.fillMaxSize()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 64.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
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
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
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