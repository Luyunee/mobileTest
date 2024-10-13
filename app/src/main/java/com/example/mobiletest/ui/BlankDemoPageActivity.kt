package com.example.mobiletest.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobiletest.datamanager.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlankDemoPageActivity : ComponentActivity() {
    val viewModel: BookingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box {
                Text(
                    text = viewModel.bookingJsonString.value,
                    Modifier.padding(20.dp),
                    fontSize = 30.sp
                )
            }
        }

        observeErrorAndToastIt()
    }

    @SuppressLint("ShowToast")
    override fun onResume() {
        super.onResume()
        viewModel.fetchBookingAndShow()
    }

    @SuppressLint("ShowToast")
    fun observeErrorAndToastIt() {
        lifecycleScope.launch(Dispatchers.Main.immediate) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorEvent.collect { errorMsg ->
                    Toast.makeText(this@BlankDemoPageActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
