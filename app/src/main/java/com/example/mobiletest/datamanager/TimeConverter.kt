package com.example.mobiletest.datamanager

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline val Int.minutes: Long get() = this * 60000L
inline val Int.seconds: Long get() = this * 1000L
