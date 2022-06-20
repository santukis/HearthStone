package com.santukis.usecases.hearthstone

import com.santukis.entities.hearthstone.Regionality
import com.santukis.usecases.UseCase
import kotlinx.coroutines.flow.Flow

interface LoadMetadataGateway {
    suspend fun loadMetadata(regionality: Regionality): Flow<Result<Unit>>
}

class LoadMetadata(private val gateway: LoadMetadataGateway): UseCase<Regionality, Flow<Result<Unit>>> {

    override suspend fun invoke(params: Regionality): Flow<Result<Unit>> = gateway.loadMetadata(regionality = params)

}