package com.example.help_code.presentation.reactive

import com.example.help_code.presentation.reactive.CounterCommand.Add
import com.example.help_code.presentation.reactive.CounterCommand.Get
import com.example.help_code.presentation.reactive.CounterCommand.Remote
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


class CoroutineActor(coroutineContext: CoroutineContext = EmptyCoroutineContext) {

    private val scope = CoroutineScope(coroutineContext)
    private var valueCounter: Int = 0
    private var counter: Int = 0

    @OptIn(ObsoleteCoroutinesApi::class)
    private val coroutineCommand = scope.actor<CounterCommand>(capacity = Channel.BUFFERED) {
        for (command in this) {
            when (command) {
                is Add -> {
                    calculate(command.counter)
                }

                is Remote -> {
                    calculate(-command.counter)
                }

                is Get -> command.response.complete(valueCounter)
            }
        }
    }
    private val runLock = ReentrantLock()

    private fun calculate(value: Int) {
        synchronized(runLock) {
            runLock.lock()
            valueCounter += value
//            Timber.i("${++counter} ${ReactiveFragment::class.java.simpleName}_value ${value}, = $valueCounter")
            runLock.unlock()
        }
    }

    suspend fun add(counter: Int) {
        delay(2000)
//        coroutineCommand.send(Add(counter))
        setCommand(Add(counter))
    }

    suspend fun remote(counter: Int) {
        delay(1000)
        setCommand(Remote(counter))
    }

    fun add2(counter: Int) {
        coroutineCommand.trySend(Add(counter))
    }

    fun remote2(counter: Int) {
        coroutineCommand.trySend(Remote(counter))
    }

    suspend fun getCounter(): Int {
        val getCounter = Get()
        setCommand(getCounter)
        return getCounter.response.await()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setCommand(command: CounterCommand) {
        GlobalScope.launch(Dispatchers.Default) {
            synchronized(runLock) {
                runBlocking { coroutineCommand.send(command) }
            }
        }
    }
}


sealed class CounterCommand {
    class Add(val counter: Int) : CounterCommand()
    class Remote(val counter: Int) : CounterCommand()
    class Get(val response: CompletableDeferred<Int> = CompletableDeferred()) : CounterCommand()
}