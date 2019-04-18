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

package com.obaralic.shade.view.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.obaralic.shade.R
import com.obaralic.shade.application.ShadeApplication
import com.obaralic.shade.util.extension.afterTextChanged
import com.obaralic.shade.util.extension.toastLong
import com.obaralic.shade.viewmodel.UserViewModel
import com.obaralic.shade.viewmodel.factory.LoginViewModelFactory
import com.obaralic.shade.viewmodel.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewmodelFactory: LoginViewModelFactory

    private lateinit var viewmodel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        appContext.toastLong("If toast is show Dagger2 is working!")
    }

    init {
        ShadeApplication.component.inject(this)
    }

    private fun init() {
        initViewModel()
        initLayout()
    }

    private fun initViewModel() {
        viewmodel = ViewModelProviders.of(this, viewmodelFactory).get(LoginViewModel::class.java)
    }

    private fun initLayout() {
        viewmodel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        viewmodel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                toastLong(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            viewmodel.dataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                viewmodel.dataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewmodel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                viewmodel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: UserViewModel) {

        // TODO : initiate successful logged in experience
        val welcome = getString(R.string.welcome)
        val name = model.name
        toastLong("$welcome $name")
    }
}
