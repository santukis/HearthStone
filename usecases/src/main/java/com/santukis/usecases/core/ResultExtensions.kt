package com.santukis.usecases.core

import kotlinx.coroutines.flow.FlowCollector

suspend fun <T> Result<T>.emitIfSuccess(flowCollector: FlowCollector<Result<T>>) {
    if (isSuccess) {
        flowCollector.emit(this)
    }
}