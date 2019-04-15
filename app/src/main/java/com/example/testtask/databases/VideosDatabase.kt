package com.example.testtask.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testtask.daos.VideoDao
import com.example.testtask.entities.VideoEntity

@Database(entities = [VideoEntity::class], version = 1)
abstract  class VideosDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {

        private var instance: VideosDatabase? = null

        @Synchronized
        fun getInstance(context: Context): VideosDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    VideosDatabase::class.java, "videos_database")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as VideosDatabase
        }
    }
}