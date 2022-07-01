package com.santukis.hearthstone.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.santukis.hearthstone.injection.UseCasesConstants.GET_DECK_USECASE
import com.santukis.hearthstone.injection.UseCasesConstants.SEARCH_CARDS_USECASE
import com.santukis.hearthstone.injection.UseCasesConstants.UPDATE_FAVOURITE_USECASE
import com.santukis.hearthstone.injection.ViewModelsConstants.VIEWMODELS_MODULE_NAME
import com.santukis.viewmodels.hearthstone.HearthstoneViewModel
import org.kodein.di.*

object ViewModelsConstants {
    const val VIEWMODELS_MODULE_NAME = "viewModels"
}

fun viewModels() = DI.Module(
    name = VIEWMODELS_MODULE_NAME,
    allowSilentOverride = true
) {

    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(di) }

    bind<ViewModel>(tag = HearthstoneViewModel::class.java.simpleName) with provider {
        HearthstoneViewModel(
            instance(tag = GET_DECK_USECASE),
            instance(tag = SEARCH_CARDS_USECASE),
            instance(tag = UPDATE_FAVOURITE_USECASE)
        )
    }
}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : DIAware, T : ViewModelStoreOwner {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}

class ViewModelFactory(private val injector: DI): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return injector.direct.instanceOrNull<ViewModel>(tag = modelClass.simpleName) as? T
            ?: modelClass.newInstance()
    }
}