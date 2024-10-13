package com.example.mobiletest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onErrorResumeNext
import kotlinx.coroutines.flow.replay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MainActivity : ComponentActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Box(Modifier.fillMaxSize()) {
                Text(
                    "Jump to blank demo page",
                    Modifier
                        .padding(20.dp)
                        .align(alignment = Alignment.Center)
                        .clickable {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    BlankDemoPageActivity::class.java
                                )
                            )
                            test()
                        }, fontSize = 30.sp
                )

            }
        }
    }

    fun test() {

        val sharedFlow = MutableSharedFlow<String>(1)
        lifecycleScope.launch {

            launch {
                delay(2000)
                sharedFlow.emit("123swe")
            }
            delay(3000)
            Log.d(TAG, "test: before")
            sharedFlow.collect { data ->
                Log.d(TAG, "test collect data: ${data}")
            }
            Log.d(TAG, "test: after collect")
        }


//        val handler = CoroutineExceptionHandler { context, throwable ->
//
//            Log.d(TAG, "handler : throwable.msg: ${throwable.message} ")
//        }
//        lifecycleScope.launch(handler) {
//            supervisorScope {
//
//                launch {
//                    coroutineScope {
//                        launch {
//                            delay(1000)
//                            throw IOException("test IOException")
//                            Log.d(TAG, "test: first launch")
//                        }
//
//                        launch {
//                            delay(3000)
//                            Log.d(TAG, "test: second launch")
//                        }
//                    }
//                }
//                launch {
//                    throw IOException("test2 IOException")
//                }
//
//            }
//            Log.d(TAG, "test: after supervisorscope")
//
//        }
    }

    override fun onResume() {
        super.onResume()
    }

}
