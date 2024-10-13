package com.example.mobiletest.datamanager

import android.content.res.AssetManager
import com.example.mobiletest.utils.AppUtils
import com.example.mobiletest.datamanager.exceptions.FetchNetworkFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicInteger


object BookingNetworkFetcher {
    private val counter = AtomicInteger(0)

    suspend fun fetchFromNetwork(url: String): String {
        return withContext(Dispatchers.IO) {
            runCatching {
                // mock networking lagging
                Thread.sleep(1000)
                val assetManager: AssetManager = AppUtils.getContext().assets
                val jsonPath: String
                if (counter.get() % 2 == 1) {
                    jsonPath = "booking_new.json"
                } else {
                    jsonPath = "booking_old.json"
                }
                val inputStream = assetManager.open(jsonPath)
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?

                while ((bufferedReader.readLine().also { line = it }) != null) {
                    stringBuilder.append(line)
                }
                val jsonString = stringBuilder.toString()
                counter.incrementAndGet()
                jsonString

            }.getOrElse { e -> throw FetchNetworkFailedException(e.message ?: "") }
        }
    }

}