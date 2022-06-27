package com.santukis.hearthstone.injection

import com.santukis.entities.hearthstone.*
import com.santukis.hearthstone.injection.RepositoriesConstants.GET_DECK_GATEWAY
import com.santukis.hearthstone.injection.RepositoriesConstants.LOAD_METADATA_GATEWAY
import com.santukis.hearthstone.injection.RepositoriesConstants.SEARCH_CARDS_GATEWAY
import com.santukis.hearthstone.injection.UseCasesConstants.GET_DECK_USECASE
import com.santukis.hearthstone.injection.UseCasesConstants.LOAD_METADATA_USECASE
import com.santukis.hearthstone.injection.UseCasesConstants.SEARCH_CARDS_USECASE
import com.santukis.hearthstone.injection.UseCasesConstants.USECASES_MODULE_NAME
import com.santukis.usecases.UseCase
import com.santukis.usecases.hearthstone.GetDeck
import com.santukis.usecases.hearthstone.LoadMetadata
import com.santukis.usecases.hearthstone.SearchCards
import kotlinx.coroutines.flow.Flow
import org.kodein.di.*

object UseCasesConstants {
    const val USECASES_MODULE_NAME = "useCases"
    const val GET_DECK_USECASE = "getDeck"
    const val SEARCH_CARDS_USECASE = "searchCards"
    const val LOAD_METADATA_USECASE = "loadMetadata"
}

fun useCases() = DI.Module(
    name = USECASES_MODULE_NAME,
    allowSilentOverride = true
) {

    bind<UseCase<DeckRequest, Flow<Result<Deck>>>>(tag = GET_DECK_USECASE) with provider {
        GetDeck(instance(GET_DECK_GATEWAY))
    }

    bind<UseCase<SearchCardsRequest, Flow<Result<List<Card>>>>>(tag = SEARCH_CARDS_USECASE) with provider {
        SearchCards(instance(SEARCH_CARDS_GATEWAY))
    }

    bind<UseCase<Regionality, Flow<Result<Unit>>>>(tag = LOAD_METADATA_USECASE) with provider {
        LoadMetadata(instance(LOAD_METADATA_GATEWAY))
    }

}