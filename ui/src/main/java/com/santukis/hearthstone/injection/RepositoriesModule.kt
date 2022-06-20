package com.santukis.hearthstone.injection

import com.santukis.hearthstone.injection.DataSourceConstants.BATTLENET_DATA_SOURCE
import com.santukis.hearthstone.injection.DataSourceConstants.ROOM_DATA_SOURCE
import com.santukis.repositories.hearthstone.HearthstoneRepository
import org.kodein.di.*

fun repositories() = DI.Module(
    name = "repositories",
    allowSilentOverride = true
) {

    bind<HearthstoneRepository>() with provider { HearthstoneRepository(instance(BATTLENET_DATA_SOURCE), instance(ROOM_DATA_SOURCE)) }
}