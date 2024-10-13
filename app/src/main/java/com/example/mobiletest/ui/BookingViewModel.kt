package com.example.mobiletest.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiletest.datamanager.DataManager
import com.example.mobiletest.utils.FlowEventBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    private val TAG = "BookingViewModel"
    private val dataManager by lazy { DataManager() }
    val errorEvent: FlowEventBus<String>
        get() = dataManager.errorEvent
    
    private val _bookingJsonString = mutableStateOf("")
    val bookingJsonString: State<String> = _bookingJsonString

    fun fetchBookingAndShow() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            launch { dataManager.fetchBooking("") }
            launch {
                dataManager.fetchBookingStateFlow.collect { data ->
                    _bookingJsonString.value = data
                }
            }
        }
    }
}