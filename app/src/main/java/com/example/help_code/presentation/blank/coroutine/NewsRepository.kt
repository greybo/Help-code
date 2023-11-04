package com.example.help_code.presentation.blank.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class NewsRepository() {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val newsRemoteDataSource: NewsRemoteDataSource = NewsRemoteDataSource()
    private val myTimber2 = Timber.tag("LatestNewsViewModel")

    fun fetchTest(time: TimeModel): Flow<TimeModel> =
        newsRemoteDataSource.testTime(time)
            .map { news -> // Executes on the default dispatcher
//                news.filter { userData.isFavoriteTopic(it) }
                news
            }
            .onEach { news -> // Executes on the default dispatcher
                Timber.tag("LatestNewsViewModel").d(news.toString())
            }
            // flowOn affects the upstream flow ↑
            .flowOn(defaultDispatcher)
//            .catch { exception -> // Executes in the consumer's context
//                Timber.tag("LatestNewsViewModel").e(exception)
//            }

}