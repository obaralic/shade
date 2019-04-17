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

package com.obaralic.shade.model

import com.obaralic.shade.model.data.User

/**
 * Class that requests authentication and user information
 * from the remote data source and maintains an in-memory
 * cache of login status and user credentials information.
 */
class LoginRepository(val dataSource: LoginDataSource) {

    /** In-memory cache of the User object. */
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null


    init {
        user = null
    }

    fun login(username: String, password: String): Result<User> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setUser(result.data)
        }

        return result
    }

    private fun setUser(user: User) {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        this.user = user
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

}