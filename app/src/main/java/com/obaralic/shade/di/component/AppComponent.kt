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

import android.app.Application
import com.obaralic.shade.App
import com.obaralic.shade.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * =====================================================================================================================
 * While using Dagger application can be separated into three layers:
 *
 * ---------------------------------------------------------------------------------------------------------------------
 * > Application Component:
 *     Used to provide application scope instances like OkHttp, Database, SharedPrefs.
 *     This is root of dagger graph and it provides three modules in our application:
 *
 *     >> AndroidInjectionModule:
 *         Internal class that provides activities and fragments with given module.
 *         This is new dagger-android class which exist in dagger-android framework.
 *
 *
 *     >> ActivityBuilder:
 *         User provided module for mapping all defined activities that are known in compile time.
 *
 *     >> AppModule:
 *         User provided module for retrofit, okhttp, persistence db, shared pref etc.
 *         Subcomponents have to be added to AppModule, so that dagger graph can understand that.
 *
 * ---------------------------------------------------------------------------------------------------------------------
 * > Activity Component:
 *     Each defined activity should have its own pair of component and module.
 *     They are defined as submodules and linked with ActivityBuilder within Application Component.
 *
 *     >> FooActivityComponent:
 *         Acts as a bridge to FooActivityModule, but there is an important change here.
 *         Methods like inject() and build() are not added into this component,
 *         since they are inherited from ancestor class.
 *
 *     >> FooActivityModule:
 *         Provides instances related with FooActivity like FooActivityViewModel etc.
 *
 * ---------------------------------------------------------------------------------------------------------------------
 * > Fragment Component:
 *     This component is to Activity Component as is Activity Component to Application Component.
 *     They are defined as submodules and linked with FragmentBuilder within Activity Component.
 *
 *     >> FooFragmentComponent:
 *         Acts as a bridge to FooFragmentModule where methods like
 *         inject() and build() are inherited from ancestor class.
 *
 *     >> FooFragmentModule:
 *         Provides instances related with FooFragment like FooFragmentViewModel etc.
 *
 * =====================================================================================================================
 * Once all components are provided all that needs to be done is to inject into DispatchingAndroidInjector<T>
 * In application containing activities HasActivityInjector needs to be implemented.
 * In activity containing fragments HasSupportFragmentInjector needs to be implemented.
 *
 * =====================================================================================================================
 * Activities and fragments should not know about how it is injected.
 * In activity injection is performed within onCreate() using AndroidInjection.inject(this).
 * In fragment injection is performed within onAttach() using AndroidSupportInjection.inject(this).
 *
 * Simplification can be gained by directly contributing dagger graph by attaching
 * activities and fragments via ContributesAndroidInjector annotation within ActivityBuilder module.
 * =====================================================================================================================
 * @see <a href="https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe">Dagger2 - Part 1</a>
 * @see <a href="https://medium.com/@iammert/new-android-injector-with-dagger-2-part-2-4af05fd783d0">Dagger2 - Part 2</a>
 */

@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        FragmentModule::class,
        AndroidSupportInjectionModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>
}