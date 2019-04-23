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

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    fun insert(user: UserEntity)

    @Update
    fun update(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("DELETE FROM users_table")
    fun deleteAll()

    @Query("SELECT * FROM users_table WHERE uid == :id")
    fun findById(id: Long): UserEntity

    @Query("SELECT * FROM users_table WHERE uid IN(:ids)")
    fun findByIds(ids: Array<Long>): List<UserEntity>

    @Query("SELECT * FROM users_table WHERE username LIKE :username AND password LIKE :password")
    fun authenticate(username: String, password: String): UserEntity

    @Query("SELECT * FROM users_table ORDER BY display_name ASC")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("SELECT COUNT(*) FROM users_table")
    fun getCount(): Int
}