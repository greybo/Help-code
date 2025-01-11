package com.example.help_code.presentation.reactive.collect.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class NewsRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun testTime(time: TimeModel): Flow<TimeModel> = flow {
        delay(time.time)
        emit(time)

    }.flowOn(ioDispatcher)
}
