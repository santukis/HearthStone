package com.santukis.hearthstone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.santukis.entities.hearthstone.DeckRequest
import com.santukis.entities.hearthstone.EuropeLocale
import com.santukis.entities.hearthstone.Regionality
import com.santukis.hearthstone.components.SampleComposable
import com.santukis.hearthstone.injection.DataSourceConstants.BATTLENET_DATA_SOURCE
import com.santukis.hearthstone.theme.CleanArchitectureTheme
import com.santukis.repositories.hearthstone.HearthstoneDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class MainActivity : ComponentActivity(), DIAware {

    override val di: DI by closestDI()

    private val hearthstoneDataSource: HearthstoneDataSource by instance(BATTLENET_DATA_SOURCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            hearthstoneDataSource.getDeck(
                DeckRequest(
                    regionality = Regionality.Taiwan(),
                    deckCode = "AAECAQcG+wyd8AKS+AKggAOblAPanQMMS6IE/web8wLR9QKD+wKe+wKz/AL1gAOXlAOalAOSnwMA"
                )
            )
                .onSuccess { println(it) }
                .onFailure { println(it.message) }
        }

        setContent {
            CleanArchitectureTheme {
                SampleComposable()
            }
        }
    }
}