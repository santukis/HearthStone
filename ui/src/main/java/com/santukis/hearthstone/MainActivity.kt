package com.santukis.hearthstone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.santukis.hearthstone.collection.components.CardCollection
import com.santukis.hearthstone.injection.viewModel
import com.santukis.hearthstone.theme.HearthstoneTheme
import com.santukis.viewmodels.hearthstone.HearthstoneViewModel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

class MainActivity : ComponentActivity(), DIAware {

    override val di: DI by closestDI()

    private val hearthstoneViewModel: HearthstoneViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HearthstoneTheme {
                CardCollection(hearthstoneViewModel)
            }
        }
    }
}