package com.santukis.hearthstone

import android.app.Application
import android.content.Context
import com.santukis.hearthstone.injection.appModule
import org.kodein.di.DI
import org.kodein.di.DIAware

class HearthStoneApplication: Application(), DIAware {

    private var overriddenModules: DI.Module? = null

    override val di by DI.lazy(allowSilentOverride = true) {
        importAll(appModule(application = this@HearthStoneApplication), allowOverride = true)
        overriddenModules?.apply { importAll(this, allowOverride = true) }
    }

    fun overrideModules(modules: DI.Module) {
        overriddenModules = modules
    }
}

fun Context.asApp() = this.applicationContext as HearthStoneApplication