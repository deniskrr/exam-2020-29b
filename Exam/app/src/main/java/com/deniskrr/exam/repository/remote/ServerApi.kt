package com.deniskrr.exam.repository.remote

import com.deniskrr.exam.model.Request
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerApi {
    @POST("/request")
    fun recordRequest(@Body request: Request): Call<Request>

    @GET("/my/{studentName}")
    fun getRequestsOfStudent(@Path("studentName") studentName: String): Call<List<Request>>

    @GET("/open")
    fun getOpenRequests(): Call<List<Request>>

    @POST("/change")
    fun changeRequestStatus(@Body request: Request): Call<Request>
}
