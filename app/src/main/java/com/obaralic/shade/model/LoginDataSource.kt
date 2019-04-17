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
import com.obaralic.shade.util.extension.stripEmail
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*

/**
 * Class that handles authentication with login credentials
 * and retrieves user information data.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<User> {
        var result: Result<User>

        try {
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
        return User(
            id = UUID.randomUUID().toString(),
            name = username.stripEmail(),
            username = username
        )
    }
}