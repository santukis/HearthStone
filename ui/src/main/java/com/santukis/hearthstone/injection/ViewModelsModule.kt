package com.santukis.hearthstone.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.kodein.di.*

fun viewModels() = DI.Module(
    name = "viewModels",
    allowSilentOverride = true
) {

}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : DIAware, T : ViewModelStoreOwner {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}