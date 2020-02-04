package com.deniskrr.exam.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Request(
    @PrimaryKey val id: Int = 0,
    val name: String = "",
    val status: String = "",
    val student: String = "",
    val eCost: Int = 0,
    val cost: Int = 0
)