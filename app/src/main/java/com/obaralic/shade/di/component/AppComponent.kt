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

package com.obaralic.shade.di.component

import com.obaralic.shade.App
import com.obaralic.shade.di.module.AndroidModule
import com.obaralic.shade.model.source.StorageDataSource
import com.obaralic.shade.view.fragment.LoginFragment
import com.obaralic.shade.viewmodel.login.LoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidModule::class])
interface AppComponent {
    fun inject(application: App)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(storageDataSource: StorageDataSource)
    fun inject(loginFragment: LoginFragment)
}