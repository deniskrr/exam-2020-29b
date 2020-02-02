package com.deniskrr.exam.repository

import com.deniskrr.exam.model.Request
import retrofit2.Callback

interface Repository {
    fun recordRequest(request: Request, callback: Callback<Request>)
    fun getRequestsOfStudent(studentName: String, callback: Callback<List<Request>>)
    fun getOpenRequests(callback: Callback<List<Request>>)
}