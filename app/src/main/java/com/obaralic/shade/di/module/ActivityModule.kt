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

import com.obaralic.shade.di.annotation.ActivityScope
import com.obaralic.shade.view.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * The ActivityModule generates AndroidInjector
 * for Activities defined in this class.
 * This allows us to inject things into Activities using
 * AndroidInjection.inject(this) in the onCreate() method.
 */
@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
