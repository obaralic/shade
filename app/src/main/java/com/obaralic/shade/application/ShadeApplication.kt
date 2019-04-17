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

package com.obaralic.shade.application

import android.app.Application
import com.obaralic.shade.dagger.component.AppComponent
import com.obaralic.shade.dagger.component.DaggerAppComponent
import com.obaralic.shade.dagger.module.ContextModule

class ShadeApplication : Application() {

    // A way of exposing Component object so that dependency can be injected,
    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
                .builder()
                .contextModule(ContextModule(this.applicationContext))
                .build()
    }
}