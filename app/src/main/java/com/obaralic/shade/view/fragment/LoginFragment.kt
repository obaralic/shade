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

package com.obaralic.shade.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.obaralic.shade.R
import com.obaralic.shade.application.ShadeApplication
import com.obaralic.shade.databinding.FragmentLoginBinding
import com.obaralic.shade.util.extension.TAG
import com.obaralic.shade.util.extension.afterTextChanged
import com.obaralic.shade.util.extension.toastLong
import com.obaralic.shade.viewmodel.factory.LoginViewModelFactory
import com.obaralic.shade.viewmodel.login.LoginFormState
import com.obaralic.shade.viewmodel.login.LoginViewModel
import com.obaralic.shade.viewmodel.login.ResultViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewmodelFactory: LoginViewModelFactory

    private lateinit var viewmodel: LoginViewModel

    private lateinit var dataBinding: FragmentLoginBinding

    companion object {
        fun new(): LoginFragment = LoginFragment()
    }

    init {
        ShadeApplication.component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "LoginFragment: onCreate")
        viewmodel = ViewModelProviders.of(this, viewmodelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "LoginFragment: onCreateView")
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // Static binding just for the sake of usage...
        dataBinding.signupButton = R.string.action_sign_up
        dataBinding.loginButton = R.string.action_log_in
        dataBinding.viewmodel = viewmodel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
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

            loading.visibility = View.GONE
            handleResult(result)
        }

        viewmodel.inputLiveState.observe(this@LoginFragment, inputObserver)
        viewmodel.loginLiveResult.observe(this@LoginFragment, resultObserver)

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
        }
    }

    private fun handleResult(result: ResultViewModel) {
        if (result.error != null) {
            toastLong(result.error)
        }
        if (result.success != null) {
            toastLong(result.success.name)
        }
    }

    private fun toastLong(text: String) {
        context?.toastLong(text)
    }

    private fun toastLong(textId: Int) {
        context?.toastLong(textId)
    }

}