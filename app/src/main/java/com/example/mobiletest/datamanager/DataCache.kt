package com.example.mobiletest.datamanager

import android.util.Log
import androidx.collection.LruCache
import com.example.mobiletest.datamanager.exceptions.CacheGetException
import com.example.mobiletest.datamanager.exceptions.CachePutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object DataCache {
    val TAG = "DataCache"
    private val maxMemory = (Runtime.getRuntime().totalMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8
    private val lruCache by lazy { LruCache<Any, CacheInfo>(cacheSize) }

    /**
     * Throw CachePutException when error
     */
    suspend fun put(key: Any, value: Any, duration: Long) =
        withContext(Dispatchers.IO) {
            runCatching {
                // mock delay
                delay(100)
                lruCache.put(
                    key,
                    CacheInfo(
                        startTime = System.currentTimeMillis(),
                        duration = duration,
                        value = value
                    )
                )
            }.getOrElse { e -> throw CachePutException(e.message ?: "") }
        }

    /**
     * Throw CacheGetException when error
     */
    suspend fun get(key: Any): CacheInfo? =
        withContext(Dispatchers.IO) {
            runCatching {
                // mock delay
                delay(100)
                val info = lruCache[key]
                info
            }.getOrElse { e -> throw CacheGetException(e.message ?: "") }
        }
}

class CacheInfo(val startTime: Long, val duration: Long, val value: Any) {

    fun isExpired(): Boolean {
        val expired = System.currentTimeMillis() - startTime > duration
        Log.d(
            DataCache.TAG,
            "isExpired: startime: ${startTime},  duration: ${duration}, different: ${System.currentTimeMillis() - startTime}"
        )
        return expired
    }
}
