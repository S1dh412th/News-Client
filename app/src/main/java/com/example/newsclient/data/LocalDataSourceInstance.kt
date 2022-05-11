package com.example.newsclient.data

import android.content.Context
import androidx.room.Room
import com.example.newsclient.data.db.ArticleDatabase

object LocalDataSourceInstance {

    @Volatile
    private var INSTANCE: ArticleDatabase? = null

    fun getDatabase(context: Context): ArticleDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "articles"
            ).build()
            INSTANCE = instance

            instance
        }
    }

}