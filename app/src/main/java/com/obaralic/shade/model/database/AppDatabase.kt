/*
 * Copyright (C) 2019 Obaralic.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.obaralic.shade.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        private const val NAME = "shade_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val parentJob = Job()

        private val coroutineContext: CoroutineContext
            get() = parentJob + Dispatchers.IO

        private val ioScope = CoroutineScope(coroutineContext)

        /**
         * Returns an instance of the Database.
         */
        fun getInstance(application: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(application)
            }


        private fun buildDatabase(application: Context) =
            Room.databaseBuilder(application.applicationContext, AppDatabase::class.java,  NAME)
                .addCallback(seedDatabaseCallback(application))
                .build()

        private fun seedDatabaseCallback(application: Context): Callback {
            return object : Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioScope.launch {
                        val dao = getInstance(application).userDao()
                        dao.insert(UserEntity(0, "debug0@test.com", "Debug0", "debug0"))
                        dao.insert(UserEntity(0, "debug1@test.com", "Debug1", "debug1"))
                        dao.insert(UserEntity(0, "debug2@test.com", "Debug2", "debug2"))
                    }
                }
            }
        }
    }
}
