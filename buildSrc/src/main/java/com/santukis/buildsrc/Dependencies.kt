package com.santukis.buildsrc

class Dependencies {

    object Test {
        const val junit4 = "junit:junit:${Versions.Test.junit4}"
        const val junit5 = "org.junit.jupiter:junit-jupiter:${Versions.Test.junit5}"
        const val mockK = "io.mockk:mockk:${Versions.Test.mockk}"
        const val mockKJvm = "io.mockk:mockk-agent-jvm:${Versions.Test.mockk}"
    }

    object AndroidTest {
        const val junit4 = "androidx.test.ext:junit:${Versions.AndroidTest.junit4}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.AndroidTest.espressoCore}"
        const val composeUi = "androidx.compose.ui:ui-test-junit4:${Versions.Androidx.compose}"
        const val mockKAndroid = "io.mockk:mockk-android:${Versions.Test.mockk}"
    }

    object Androidx {
        const val core = "androidx.core:core-ktx:${Versions.Androidx.core}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.Androidx.activityCompose}"
        const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.Androidx.compose}"
        const val composeUi = "androidx.compose.ui:ui:${Versions.Androidx.compose}"
        const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.Androidx.compose}"
        const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.Androidx.compose}"
        const val composeMaterial = "androidx.compose.material:material:${Versions.Androidx.compose}"
        const val composeMaterialIcons = "androidx.compose.material:material-icons-core:${Versions.Androidx.compose}"
        const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.Androidx.compose}"
    }

    object Kodein {
        const val kodein = "org.kodein.di:kodein-di:${Versions.Kodein.kodein}"
        const val kodeinAndroidFramework = "org.kodein.di:kodein-di-framework-android-x:${Versions.Kodein.kodein}"
        const val kodeinConfJvm = "org.kodein.di:kodein-di-conf-jvm:${Versions.Kodein.kodein}"
    }

    object Kotlin {
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.coroutinesCore}"
    }
}