package com.santukis.cleanarquitecture

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.santukis.hearthstone.asApp
import org.junit.Before
import org.junit.runner.RunWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.DI

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
abstract class AcceptanceTest {

    @Before
    fun setup() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()
        app.overrideModules(testDependencies)
    }

    protected fun launch(
        intent: Intent,
        initialState: Lifecycle.State = Lifecycle.State.RESUMED,
        testBlock: ActivityScenario<Activity>.() -> Unit
    ) {
        ActivityScenario.launch<Activity>(intent).use { scenario ->
            scenario.moveToState(initialState)
            scenario.testBlock()
        }
    }

    abstract val testDependencies: DI.Module
}