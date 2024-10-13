package com.example.mobiletest.datamanager

import android.util.Log
import com.example.mobiletest.utils.FlowEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class DataManager {
    private val TAG = "DataManager"
    private val _fetchBookingStateFlow = MutableStateFlow("")
    val fetchBookingStateFlow: StateFlow<String> = _fetchBookingStateFlow
    val errorEvent = FlowEventBus<String>()

    suspend fun fetchBooking(url: String) {
        // if getting cache runs into an error, cache will be null.
        val cache = runCatching { DataCache.get(url) }.onFailure { e ->
            errorEvent.post(e.message ?: "DataCache get() error")
        }.getOrNull()
        if (cache == null) {
            // Hasn't been cached, fetch network and cache
            fetchNetworkAndCache(url)
        } else if (cache.value is String) {
            // invoking order is important
            _fetchBookingStateFlow.value = cache.value
            if (cache.isExpired()) {
                fetchNetworkAndCache(url)
            }
        }
    }

    /**
     * fetch network for the most update-to-updated data and return it, and by the way cache it.
     */
    private suspend fun fetchNetworkAndCache(url: String) {
        runCatching {
            BookingNetworkFetcher.fetchFromNetwork(url)
        }.onSuccess { value ->
            _fetchBookingStateFlow.emit(value)
            runCatching {
                // 5 minutes cache
                DataCache.put(
                    key = url,
                    value = value,
                    duration = 5.minutes
                )
            }.onFailure { e ->
                delay(1000)
                errorEvent.post(e.message ?: "DataCache put() error")
            }
        }.onFailure { e ->
            errorEvent.post(e.message ?: "BookingNetworkFetcher fetchFromNetwork error")
        }
    }

}