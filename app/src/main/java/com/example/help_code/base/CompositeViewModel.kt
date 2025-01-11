package com.example.help_code.base

//import au.com.crownresorts.crma.utility.SingleLiveEvent
//import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

interface BaseViewModel {
//    val errorsStream: LiveData<Throwable>
}

open class CompositeViewModel : ViewModel(), BaseViewModel {


    @Volatile
    var isCleared: Boolean = false

    override fun onCleared() {
        isCleared = true
        super.onCleared()
    }


    val errorsStream = MutableLiveData<Throwable>()


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        errorsStream.postValue(exception)
    }
    val scopeDefault get() = CoroutineScope(Dispatchers.Default + exceptionHandler)


    protected fun launchOn(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Default + exceptionHandler, block = block)
    }
}