package com.deniskrr.exam.extensions

import org.json.JSONObject
import retrofit2.Response

fun <T> Response<T>.getErrorMessage(): String {
    val jObjError = JSONObject(this.errorBody()!!.string())
    return jObjError.getString("text")
}