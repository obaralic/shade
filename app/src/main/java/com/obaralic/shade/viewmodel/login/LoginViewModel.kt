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

package com.obaralic.shade.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.obaralic.shade.R
import com.obaralic.shade.model.LoginRepository
import com.obaralic.shade.model.Result
import com.obaralic.shade.util.extension.isEmailAddress
import com.obaralic.shade.viewmodel.UserViewModel

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    companion object {
        const val LENGTH = 5
    }

    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val _loginResult = MutableLiveData<ResultViewModel>()
    val loginResult: LiveData<ResultViewModel> = _loginResult

    fun login(username: String, password: String) {
        // TODO: Launched in a separate asynchronous job via Rx
        val result = repository.login(username, password)
        when (result) {
            is Result.Success ->
                _loginResult.value = ResultViewModel(
                    success = UserViewModel(
                        name = result.data.name,
                        username = result.data.username
                    )
                )

            is Result.Error ->
                _loginResult.value = ResultViewModel(error = R.string.login_failed)
        }
    }

    fun dataChanged(username: String, password: String) {
        if (!isUsernameValid(username)) {
            _loginFormState.value = LoginFormState(usernameError = R.string.invalid_username)

        } else if (!isPasswordValid(password)) {
            _loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)

        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    fun logout() {
        if (repository.isLoggedIn) repository.logout()
    }

    // TODO: A placeholder username validation check
    private fun isUsernameValid(username: String) = username.isEmailAddress()

    // TODO: A placeholder password validation check
    private fun isPasswordValid(password: String) = password.length > LENGTH
}
