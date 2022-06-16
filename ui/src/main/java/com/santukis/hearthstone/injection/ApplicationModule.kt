package com.santukis.hearthstone.injection

import android.app.Application
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

fun appModule(application: Application) = DI.Module(
    name = "appModule",
    allowSilentOverride = true
) {
    import(androidXModule(application))
    import(viewModels())
    import(useCases())
    import(repositories())
    import(dataSources())
}