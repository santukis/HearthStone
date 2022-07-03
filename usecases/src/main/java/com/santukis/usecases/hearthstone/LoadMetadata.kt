package com.santukis.usecases.hearthstone

import com.santukis.entities.hearthstone.Metadata
import com.santukis.entities.hearthstone.Regionality
import com.santukis.usecases.UseCase
import kotlinx.coroutines.flow.Flow

interface LoadMetadataGateway {
    suspend fun loadMetadata(regionality: Regionality): Flow<Result<Metadata>>
}

class LoadMetadata(private val gateway: LoadMetadataGateway): UseCase<Regionality, Flow<Result<Metadata>>> {

    override suspend fun invoke(params: Regionality): Flow<Result<Metadata>> = gateway.loadMetadata(regionality = params)

}