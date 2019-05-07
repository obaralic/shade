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
import com.obaralic.shade.App
import com.obaralic.shade.model.Result
import com.obaralic.shade.model.repo.LoginRepository
import com.obaralic.shade.util.extension.isEmailAddress
import com.obaralic.shade.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel : ViewModel() {

    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    @Inject
    lateinit var repository: LoginRepository

    init {
        App.component.inject(this)
    }

    // LiveData that is fed from the user input and its change is observed for the sake of button enabling.
    private val inputState: MutableLiveData<LoginFormState> by lazy { MutableLiveData<LoginFormState>() }
    val inputLiveState: LiveData<LoginFormState> by lazy { inputState }

    // LiveData that is fed from the user authentication.
    private val loginResult: MutableLiveData<ResultViewModel> by lazy { MutableLiveData<ResultViewModel>() }
    val loginLiveResult: LiveData<ResultViewModel> by lazy { loginResult }

    /**
     *
     */
    fun login(username: String, password: String) = scope.launch(Dispatchers.IO) {

        // TODO: Launched in a separate asynchronous job via Rx
        // With Rx convert postValue to setValue
        val result = repository.login(username, password)
        when (result) {
            is Result.Success ->
                loginResult.postValue(
                    ResultViewModel(
                        success = UserViewModel(
                            name = result.data.name,
                            username = result.data.username
                        )
                    )
                )

            is Result.Error ->
                loginResult.postValue(ResultViewModel(error = R.string.login_failed))
        }
    }

    /**
     *
     */
    fun signUp(username: String, password: String) = scope.launch(Dispatchers.IO) {
        val result = repository.signUp(username, password)
        when (result) {
            is Result.Error -> throw RuntimeException(result.error)
        }
    }

    /**
     *
     */
    fun inputDataChanged(username: String, password: String) {
        if (!isUsernameValid(username)) {
            inputState.value = LoginFormState(usernameError = R.string.invalid_username)

        } else if (!isPasswordValid(password)) {
            inputState.value = LoginFormState(passwordError = R.string.invalid_password)

        } else {
            inputState.value = LoginFormState(isDataValid = true)
        }
    }

    /**
     *
     */
    fun logout() = scope.launch(Dispatchers.IO) {
        if (repository.isLoggedIn) repository.logout()
    }

    // TODO: A placeholder username validation check
    private fun isUsernameValid(username: String) = username.isEmailAddress()

    // TODO: A placeholder password validation check
    private fun isPasswordValid(password: String) = password.length > 5
}
