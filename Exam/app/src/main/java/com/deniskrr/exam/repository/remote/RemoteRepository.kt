package com.deniskrr.exam.repository.remote

import com.deniskrr.exam.model.Request
import com.deniskrr.exam.repository.Repository
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRepository : Repository {
    private val api: ServerApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:2902/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create<ServerApi>(ServerApi::class.java)
    }

    override fun recordRequest(request: Request, callback: Callback<Void>) {
        api.recordRequest(request).enqueue(callback)
    }
}