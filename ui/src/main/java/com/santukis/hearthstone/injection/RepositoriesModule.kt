package com.santukis.hearthstone.injection

import com.santukis.hearthstone.injection.DataSourceConstants.BATTLENET_DATA_SOURCE
import com.santukis.hearthstone.injection.DataSourceConstants.ROOM_DATA_SOURCE
import com.santukis.hearthstone.injection.RepositoriesConstants.GET_DECK_GATEWAY
import com.santukis.hearthstone.injection.RepositoriesConstants.HEARTHSTONE_REPOSITORY
import com.santukis.hearthstone.injection.RepositoriesConstants.LOAD_METADATA_GATEWAY
import com.santukis.hearthstone.injection.RepositoriesConstants.REPOSITORIES_MODULE_NAME
import com.santukis.hearthstone.injection.RepositoriesConstants.SEARCH_CARDS_GATEWAY
import com.santukis.repositories.hearthstone.HearthstoneRepository
import com.santukis.usecases.hearthstone.GetDeckGateway
import com.santukis.usecases.hearthstone.LoadMetadataGateway
import com.santukis.usecases.hearthstone.SearchCardsGateway
import org.kodein.di.*

object RepositoriesConstants {
    const val REPOSITORIES_MODULE_NAME = "repositories"
    const val HEARTHSTONE_REPOSITORY = "hearthstoneRepository"
    const val GET_DECK_GATEWAY = "getDeckGateway"
    const val SEARCH_CARDS_GATEWAY = "searchCardsGateway"
    const val LOAD_METADATA_GATEWAY = "loadMetadata"
}

fun repositories() = DI.Module(
    name = REPOSITORIES_MODULE_NAME,
    allowSilentOverride = true
) {
    bind<HearthstoneRepository>(tag = HEARTHSTONE_REPOSITORY) with singleton {
        HearthstoneRepository(
            instance(BATTLENET_DATA_SOURCE),
            instance(ROOM_DATA_SOURCE)
        )
    }

    bind<GetDeckGateway>(tag = GET_DECK_GATEWAY) with singleton {
        instance(tag = HEARTHSTONE_REPOSITORY)
    }

    bind<SearchCardsGateway>(tag = SEARCH_CARDS_GATEWAY) with singleton {
        instance(tag = HEARTHSTONE_REPOSITORY)
    }

    bind<LoadMetadataGateway>(tag = LOAD_METADATA_GATEWAY) with singleton {
        instance(tag = HEARTHSTONE_REPOSITORY)
    }
}