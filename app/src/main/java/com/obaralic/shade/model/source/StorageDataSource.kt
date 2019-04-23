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

package com.obaralic.shade.model.source

import android.content.Context
import android.util.Log
import com.obaralic.shade.application.ShadeApplication
import com.obaralic.shade.model.Result
import com.obaralic.shade.model.data.User
import com.obaralic.shade.model.database.AppDatabase
import com.obaralic.shade.model.database.UserDao
import com.obaralic.shade.model.database.UserEntity
import com.obaralic.shade.util.extension.TAG
import com.obaralic.shade.util.extension.stripEmail
import javax.inject.Inject

/**
 * Class that handles authentication with login credentials
 * and retrieves user information data from the local data storage.
 */
class StorageDataSource @Inject constructor() {

    private val userSource: UserDao

    @Inject
    lateinit var application: Context

    init {
        ShadeApplication.component.inject(this)
        val database = AppDatabase.getInstance(application)
        userSource = database.userDao()
    }

    fun login(username: String, password: String): Result<User> {
        Log.d(TAG, "Login[$username, $password]")
        var result: Result<User>

        try {
            val user = authenticate(username, password)
            result = Result.Success(user)

        } catch(error: Exception) {
            result = Result.Error(RuntimeException("Login error in", error))
        }

        return result
    }

    fun signUp(username: String, password: String): Result<User> {
        Log.d(TAG, "SignUp[$username, $password]")
        var result: Result<User>

        try {
            val userEntity = UserEntity(1L, username, username.stripEmail(), password)
            userSource.insert(userEntity)
            val user = authenticate(username, password)
            result = Result.Success(user)

        } catch(error: Exception) {
            result = Result.Error(RuntimeException("Login error in", error))
        }

        return result
    }

    fun logout() {
        // TODO: Revoke authentication

    }

    private fun authenticate(username: String, password: String): User {
        // TODO: handle user authentication
        val userEntity: UserEntity = userSource.authenticate(username, password)
        return User(
            id = userEntity.userId,
            name = userEntity.name,
            username = userEntity.username
        )

    }
}