package com.example.mobiletest.datamanager.exceptions

import java.io.IOException

class FetchNetworkFailedException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}