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
import android.content.Context
import android.location.LocationManager
import com.obaralic.shade.dagger.component.AppComponent
import com.obaralic.shade.dagger.component.DaggerAppComponent
import com.obaralic.shade.dagger.module.AndroidModule
import com.obaralic.shade.util.extension.toastLong
import javax.inject.Inject

class ShadeApplication : Application() {

    // A way of exposing Component object so that dependency can be injected,
    companion object {
        lateinit var component: AppComponent
    }

    @Inject
    lateinit var manager: LocationManager

    @Inject
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
                .builder()
                .androidModule(AndroidModule(this))
                .build()
        component.inject(this)

        // Test DI success
        context.toastLong("Available providers:\n${manager.allProviders}")
    }
}