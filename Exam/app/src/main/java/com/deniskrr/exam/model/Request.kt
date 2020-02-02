package com.deniskrr.exam.model

data class Request(
    val id: Int = 0,
    val name: String = "",
    val status: String = "",
    val student: String = "",
    val eCost: Int = 0,
    val cost: Int = 0
)