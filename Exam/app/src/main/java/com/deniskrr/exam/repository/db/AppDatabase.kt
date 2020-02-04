package com.deniskrr.exam.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deniskrr.exam.model.Request

@Database(entities = [Request::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun requestDao(): RequestDao
}