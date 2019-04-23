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

package com.obaralic.shade.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.obaralic.shade.viewmodel.login.LoginViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

/**
 * {@link LoginViewModelFactory} is used to instantiate {@link LoginViewModel}.
 * Required given {@link LoginViewModel} has a non-empty constructor.
 */
@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory @Inject constructor() : ViewModelProvider.Factory {

    override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as VM
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
