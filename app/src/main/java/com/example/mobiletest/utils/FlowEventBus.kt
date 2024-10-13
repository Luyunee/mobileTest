package com.example.mobiletest.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FlowEventBus<T>() {
    private val _event = MutableSharedFlow<T>()
    val events = _event.asSharedFlow()
    suspend fun post(event: T) = _event.emit(event)
    fun post(scope: CoroutineScope, event: T) = scope.launch { _event.emit(event) }

    suspend inline fun collect(crossinline action: suspend (value: T) -> Unit) {
        events.collect { action(it) }
    }

    inline fun collect(scope: CoroutineScope, crossinline action: suspend (value: T) -> Unit) {
        scope.launch {
            events.collect { action(it) }
        }
    }


    inline fun collect(
        lifecycleOwner: LifecycleOwner,
        crossinline action: suspend (value: T) -> Unit
    ) {
        lifecycleOwner.lifecycleScope.launch { events.collect { action(it) } }
    }
}