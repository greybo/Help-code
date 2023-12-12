package com.example.help_code.presentation.blank

import com.example.help_code.base.CompositeViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import timber.log.Timber

class ExceptionCoroutineViewModel : CompositeViewModel() {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var testString: String? = null
    fun runTask() {
        val cd = runTaskRX()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Timber.e("runTask error")
            }
            .subscribe {
                Timber.d("runTask subscribe")
            }
    }

    private fun runTaskRX(): Completable {
        return Completable.create {
            scope.launch {
                try {
                    val result = taskException()
                    Timber.d("try runTaskRX: $result")
                    withContext(NonCancellable) {
                        if (result != null) it.onComplete() else throw NullPointerException()
                    }
                } catch (e: Exception) {
                    Timber.e("catch runTaskRX:")
                    Timber.e(e)
                    it.onError(e)
                }
            }
        }.onErrorComplete()
    }

    private suspend fun taskException(): String? {

        delay(300)
//        testString = "test result"
        return testString!!
//        throw NullPointerException()
    }
}