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

package com.obaralic.shade.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.obaralic.shade.viewmodel.factory.ViewModelFactory
import com.obaralic.shade.di.ViewModelKey
import com.obaralic.shade.viewmodel.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * The ViewModelModule is used to provide a map of view models
 * through dagger that is used by the ViewModelFactory class.
 * - We can use the ViewModelModule to define our ViewModels.
 * - We provide a key for each ViewModel using the ViewModelKey class.
 * - Then in our Activity/Fragment, we use the ViewModelFactory class to inject the corresponding ViewModel.
 */
@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * This method basically injects this object into a Map
     * using the @IntoMap annotation, with the LoginViewModel.class as key
     * and a Provider that will build a LoginViewModel object.
     */
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

}
