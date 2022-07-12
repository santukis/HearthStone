package com.santukis.hearthstone.collection.components

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.santukis.entities.core.takeIfNotEmpty
import com.santukis.hearthstone.core.components.HtmlText
import com.santukis.hearthstone.theme.WhiteTransparent
import com.santukis.viewmodels.R
import com.santukis.viewmodels.entities.*
import com.santukis.viewmodels.entities.CardFilterState.Companion.KEYWORD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CardDetailContent(
    scaffoldState: BottomSheetScaffoldState,
    cardDetailState: CardDetailState,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onUiEvent: (UiEvent) -> Unit = {},
    onHeaderHeightChange: (Int) -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CardDetailActionBar(
            scaffoldState = scaffoldState,
            cardDetailState = cardDetailState,
            coroutineScope = coroutineScope,
            onUiEvent = onUiEvent
        )

        Column(
            modifier = Modifier
                .background(WhiteTransparent)
                .fillMaxSize()
                .padding(
                    top = 8.dp
                )
                .verticalScroll(rememberScrollState()),
        ) {

            cardDetailState.card?.let { card ->
                CardDetailHeader(
                    cardDetailState = cardDetailState,
                    onHeaderHeightChange = onHeaderHeightChange
                )

                card.cardText.ruleText.takeIfNotEmpty()?.let { text ->
                    HtmlText(
                        html = card.cardText.ruleText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                        font = R.font.belwe_medium_bt
                    )
                }

                FieldText(field = card.cardSet.identity.name, headerName = com.santukis.hearthstone.R.string.set)

                FieldText(field = card.cardClass.identity.name, headerName = com.santukis.hearthstone.R.string.card_class)

                FieldText(field = card.cardType.identity.name, headerName = com.santukis.hearthstone.R.string.type)

                cardDetailState.getRarityText().let { rarity ->
                    Text(
                        text = stringResource(id = com.santukis.hearthstone.R.string.rarity),
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                        style = MaterialTheme.typography.h5,
                    )

                    Row(
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = rarity.text,
                            style = MaterialTheme.typography.subtitle1
                        )

                        Image(
                            painter = painterResource(cardDetailState.getRarityDrawable()),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(24.dp)
                        )
                    }
                }

                FieldText(field = card.spellSchool.identity.name, headerName = com.santukis.hearthstone.R.string.spell_school)

                if (cardDetailState.relatedCards.isNotEmpty()) {
                    Text(
                        text = stringResource(id = com.santukis.hearthstone.R.string.related_cards),
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                        ),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.h5,
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        items(cardDetailState.relatedCards.size) { index ->
                            val relatedCard = cardDetailState.relatedCards[index]

                            AsyncImage(
                                model = relatedCard.images.image,
                                contentDescription = "",
                                modifier = Modifier.defaultMinSize(
                                    minWidth = 200.dp,
                                    minHeight = 350.dp
                                )
                            )
                        }
                    }
                }

                cardDetailState.card?.keywords?.takeIfNotEmpty()?.let { keywords ->
                    Text(
                        text = stringResource(id = com.santukis.hearthstone.R.string.keywords),
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.h5,
                    )

                    Text(
                        text = stringResource(com.santukis.hearthstone.R.string.keyword_interaction_message),
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.caption,
                    )

                    LazyRow(
                        contentPadding = PaddingValues(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        items(keywords) { keyword ->
                            Row(
                                modifier = modifier
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(size = 30.dp))
                                    .background(MaterialTheme.colors.secondary)
                                    .combinedClickable(
                                        onClick = { onUiEvent(OnFilterSelected(KEYWORD, keyword.asCardFilter())) },
                                        onLongClick = {
                                            coroutineScope.launch {
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    message = keyword.cardText.ruleText,
                                                    duration = SnackbarDuration.Indefinite,
                                                    actionLabel = "Close"
                                                )
                                            }
                                        }
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround

                            ) {

                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = keyword.getName(),
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardDetailActionBar(
    scaffoldState: BottomSheetScaffoldState,
    cardDetailState: CardDetailState,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onUiEvent: (UiEvent) -> Unit = {}
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
                    coroutineScope.launch {
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
                onUiEvent(OnFavouriteClick())
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
}

@Composable
fun CardDetailHeader(
    cardDetailState: CardDetailState,
    onHeaderHeightChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .onGloballyPositioned {
                onHeaderHeightChange(it.size.height)
            }
            .padding(
                start = 16.dp,
                end = 16.dp
            )
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.mana),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp)
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
                text = cardDetailState.getName(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 50.dp,
                        end = 50.dp
                    ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h4,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = cardDetailState.getFlavorText(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun FieldText(
    field: String,
    @StringRes headerName: Int
) {
    field.takeIfNotEmpty()?.let { fieldText ->
        Text(
            text = stringResource(id = headerName),
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            style = MaterialTheme.typography.h5,
        )

        Text(
            text = fieldText,
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            style = MaterialTheme.typography.subtitle1,
        )
    }
}