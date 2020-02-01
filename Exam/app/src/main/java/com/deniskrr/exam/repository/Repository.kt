package com.deniskrr.exam.repository

import com.deniskrr.exam.model.Request
import retrofit2.Callback

interface Repository {
    fun recordRequest(request: Request, callback: Callback<Void>)
}