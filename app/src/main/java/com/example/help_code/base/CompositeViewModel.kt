package com.example.help_code.base

//import au.com.crownresorts.crma.utility.SingleLiveEvent
//import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface BaseViewModel {
//    val errorsStream: LiveData<Throwable>
}

open class CompositeViewModel : ViewModel(), BaseViewModel {

    protected open val handler = CoroutineExceptionHandler { _, exception ->
//        errorsStream.postValue(exception)
    }

//    override val errorsStream = SingleLiveEvent<Throwable>()

    protected fun launchOn(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(handler, block = block)
    }

    @Volatile
    var isCleared: Boolean = false
//    val cd = CompositeDisposable()

    override fun onCleared() {
        isCleared = true
        super.onCleared()
//        if (!cd.isDisposed) {
//            cd.dispose()
//        }
    }


}