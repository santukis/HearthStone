package com.santukis.usecases

interface UseCase<Params, Result> {
    suspend operator fun invoke(params: Params): Result
}