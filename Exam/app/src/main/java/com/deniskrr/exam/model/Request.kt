package com.deniskrr.exam.model

data class Request(
    val name: String,
    val status: String,
    val student: String,
    val eCost: Int,
    val cost: Int
)