package com.deniskrr.exam.repository.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.deniskrr.exam.model.Request

interface RequestDao {

    @Query("SELECT * FROM request")
    fun getAll(): List<Request>

    @Insert
    fun insert(request: Request)

    @Delete
    fun delete(request: Request)
}