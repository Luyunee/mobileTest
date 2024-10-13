package com.example.mobiletest.utils

import android.app.Application

object AppUtils {
    private lateinit var app: Application
    fun app(app: Application) {
        AppUtils.app = app
    }

    fun getContext(): Application = app
}