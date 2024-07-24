package com.example.help_code.presentation.reactive.actor

import com.example.help_code.presentation.reactive.actor.CounterCommand.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class CoroutineActor(coroutineContext: CoroutineContext = EmptyCoroutineContext) {

    private val scope = CoroutineScope(coroutineContext)
    private var counter: Int = 0

    @OptIn(ObsoleteCoroutinesApi::class)
    private val coroutineCommand = scope.actor<CounterCommand>(capacity = Channel.BUFFERED) {
        for (command in this) {
            when (command) {
                is Add -> counter += command.counter
                is Remote -> counter -= command.counter
                is Get -> command.response.complete(counter)
            }
        }
    }

    suspend fun add(counter: Int) {
        delay(2000)
        coroutineCommand.send(Add(counter))
    }

    suspend fun remote(counter: Int) {
        delay(1000)
        coroutineCommand.send(Remote(counter))
    }

    fun add2(counter: Int) {
        coroutineCommand.trySend(Add(counter))
    }

    fun remote2(counter: Int) {
        coroutineCommand.trySend(Remote(counter))
    }

    suspend fun getCounter(): Int {
        val getCounter = Get()
        coroutineCommand.send(getCounter)
        return getCounter.response.await()
    }
}


sealed class CounterCommand {
    class Add(val counter: Int) : CounterCommand()
    class Remote(val counter: Int) : CounterCommand()
    class Get(val response: CompletableDeferred<Int> = CompletableDeferred()) : CounterCommand()
}