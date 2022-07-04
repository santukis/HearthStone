package com.santukis.hearthstone.collection.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santukis.hearthstone.R
import com.santukis.hearthstone.theme.HearthstoneTheme
import com.santukis.viewmodels.entities.FilterUI

@Composable
fun ActiveFilter(
    filter: FilterUI<Int, String>,
    modifier: Modifier = Modifier,
    onRemoveFilterClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(size = 30.dp))
            .background(MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround

    ) {

        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Remove Filter",
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .clickable {
                    onRemoveFilterClick()
                }
        )

        Text(
            modifier = Modifier.padding(4.dp),
            text = stringResource(
                id = filter.key,
                filter.value
            ),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Preview
@Composable
fun ActiveFilterPreview() {
    HearthstoneTheme {
        ActiveFilter(filter = FilterUI(R.string.mana_cost, "10"))
    }
}