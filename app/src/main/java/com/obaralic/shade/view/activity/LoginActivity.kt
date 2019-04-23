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
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.obaralic.shade.R
import com.obaralic.shade.application.ShadeApplication
import com.obaralic.shade.util.extension.afterTextChanged
import com.obaralic.shade.util.extension.toastLong
import com.obaralic.shade.viewmodel.UserViewModel
import com.obaralic.shade.viewmodel.factory.LoginViewModelFactory
import com.obaralic.shade.viewmodel.login.LoginFormState
import com.obaralic.shade.viewmodel.login.LoginViewModel
import com.obaralic.shade.viewmodel.login.ResultViewModel
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
        val inputObserver = Observer<LoginFormState> {

            // Null check of the state
            val state = it ?: return@Observer

            login.isEnabled = state.isDataValid
            if (state.usernameError != null) username.error = getString(state.usernameError)
            if (state.passwordError != null) password.error = getString(state.passwordError)
        }

        val resultObserver = Observer<ResultViewModel> {

            val result = it ?: return@Observer

            login.visibility = GONE
            if (result.error != null) toastLong(result.error)
            if (result.success != null) toastLong(result.success.name)

            //Complete and destroy login activity once successful
            setResult(Activity.RESULT_OK)
            finish()
        }

        viewmodel.inputLiveState.observe(this@LoginActivity, inputObserver)
        viewmodel.loginLiveResult.observe(this@LoginActivity, resultObserver)

        username.afterTextChanged {
            viewmodel.inputDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                viewmodel.inputDataChanged(
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

        signup.setOnClickListener{
            toastLong("Dummy action: Inserting debug database item.")
//            viewmodel.signUp("debug@test.com", "debug@test")
        }
    }

    private fun updateLayout(model: UserViewModel) {

        // TODO : initiate successful logged in experience
        val welcome = getString(R.string.welcome)
        val name = model.name
        toastLong("$welcome $name")
    }
}
