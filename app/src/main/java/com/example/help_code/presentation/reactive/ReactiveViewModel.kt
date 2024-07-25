package com.example.help_code.presentation.reactive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.help_code.base.CompositeViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.locks.ReentrantLock

class ReactiveViewModel : CompositeViewModel() {
    val tag = "ReactiveFragment"

    private val lock = ReentrantLock();
    private var isFetched: Boolean = false

    private var actorExample = CoroutineActor()
    private val listBackup = mutableListOf<String>()
    private val innerItemsFlow = MutableLiveData<List<String>>()
    val itemsFlow: LiveData<List<String>> = innerItemsFlow

    fun syncTest() {
        listBackup.clear()
        addToList("------syncTest------")
        isFetched = false

        viewModelScope.launch(Dispatchers.Main) {
            syncExecute("Main 1.")
        }
        viewModelScope.launch(Dispatchers.Default) {
            syncExecute("Default 2.")
        }
        viewModelScope.launch(Dispatchers.IO) {
            syncExecute("IO 3.")
        }
        viewModelScope.launch(Dispatchers.Main) {
            syncExecute("Main 4.")
        }
        viewModelScope.launch(Dispatchers.IO) {
            syncExecute("IO 5.")
        }
        viewModelScope.launch(Dispatchers.Default) {
            syncExecute("Default 6.")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun syncExecute(name: String) {
        GlobalScope.launch(Dispatchers.Default) {
            delay(10)
            synchronized(lock) {
                addToList("$name runSync")
                runBlocking {
                    if (isFetched) {
                        addToList("$name runSync already")
                    } else {
                        isFetched = callRest()
                        addToList("$name runSync result: $isFetched")
                    }
                }
            }
        }
    }

    private suspend fun callRest(): Boolean {
        val deferred: CompletableDeferred<Boolean> = CompletableDeferred()
        delay(2000)
        deferred.complete(true)
        return deferred.await()
    }

    fun actorTest() {
        listBackup.clear()
        addToList("-------actorTest--------")
        addToList("Actor test - Start")
        viewModelScope.launch(Dispatchers.Main) {
            actorExecute(56, "Main 1.")
        }
        viewModelScope.launch(Dispatchers.Default) {
            actorExecute(-56, "Default 2.")
        }
        viewModelScope.launch(Dispatchers.IO) {
            actorExecute(-56, "IO 3.")
        }
        viewModelScope.launch(Dispatchers.Main) {
            actorExecute(56, "Main 4.")
        }
        viewModelScope.launch(Dispatchers.IO) {
            actorExecute(-56, "IO 5.")
        }
        viewModelScope.launch(Dispatchers.Default) {
            actorExecute(56, "Default 6.")
        }
    }

    private fun actorExecute(value: Int, name: String) {
        viewModelScope.launch(Dispatchers.Default) {
            delay(10)
            actorExample.add(value)
            val result = actorExample.getCounter()
            addToList("$name ${if (value < 0) "Remove" else "Add"}($value): result: $result")
        }
    }

    //================================================================
    fun test3() = runBlocking<Unit> {
        addToList("-----------------")

        val a = (1..5).asFlow()
        val b = (6..10).asFlow()
        val c = (11..19).asFlow()
        a
            .zip(b) { x, y -> "zip b: $x + $y" }
            .zip(c) { x, y -> "zip c: $x + $y" }
            .collect { addToList(it) }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun test2() = runBlocking<Unit> {
        addToList("------------------")
        val flow1 = flowOf("A", "B", "C")
        val flow2 = flowOf("D", "E", "F")
        val flow3 = flowOf("G", "H", "I")

        flowOf(flow1, flow2, flow3)
            .flatMapConcat { it }
            .collect {
                delay(500)
                addToList("flatMapConcat: $it")
            }
    }


    fun test1() = runBlocking<Unit> {
        addToList("------------------")
        val numbers = (1..5).asFlow()
        numbers.transform { value ->
            emit("A$value")
            emit("B$value")
            delay(500)
        }.collect { addToList("transform: $it") }
    }

    fun test0() = runBlocking<Unit> {
        addToList("-----------------")
        test().take(2) // take only the first two
            .collect { value ->
                delay(500)
                addToList("take: $value")
            }
    }

    private fun test(): Flow<Int> = flow {
        addToList("-----------------")
        try {
            emit(1)
            emit(2)
            addToList("This line will not execute")
            emit(3)
        } finally {
            addToList("Finally in numbers")
        }
    }
//=====================================================

    private fun addToList(value: String) {
        Timber.tag(tag).i(value)
        listBackup.add(value)
        innerItemsFlow.postValue(listBackup)
    }
}