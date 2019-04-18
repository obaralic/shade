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

package com.obaralic.shade.dagger.component

import com.obaralic.shade.application.ShadeApplication
import com.obaralic.shade.dagger.module.AndroidModule
import com.obaralic.shade.view.activity.BaseActivity
import com.obaralic.shade.view.activity.LoginActivity
import com.obaralic.shade.viewmodel.login.LoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidModule::class])
interface AppComponent {
    fun inject(application: ShadeApplication)
    fun inject(base: BaseActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(loginViewModel: LoginViewModel)
}