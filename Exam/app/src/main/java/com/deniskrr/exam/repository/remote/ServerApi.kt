package com.deniskrr.exam.repository.remote

import com.deniskrr.exam.model.Request
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApi {
    @POST("/request")
    fun recordRequest(@Body request: Request): Call<Void>
}
