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

package com.obaralic.shade

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.util.Log
import com.obaralic.shade.di.component.AppComponent
import com.obaralic.shade.di.component.DaggerAppComponent
import com.obaralic.shade.di.module.AndroidModule
import com.obaralic.shade.model.database.AppDatabase
import com.obaralic.shade.util.extension.TAG
import com.obaralic.shade.util.ioThread
import com.obaralic.shade.util.log.CrashReportingTree
import javax.inject.Inject
import timber.log.Timber.DebugTree
import timber.log.Timber



class App : Application() {

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
        initTimber()
        initDagger()
        initDatabase()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        else Timber.plant(CrashReportingTree())
    }

    private fun initDagger() {
        component = DaggerAppComponent
            .builder()
            .androidModule(AndroidModule(this))
            .build()
        component.inject(this)
    }

    private fun initDatabase() {
        ioThread {
            // Just accessing database so that it can be created.
            val userDao = AppDatabase.getInstance(this).userDao()
            Timber.d("Users no: ${userDao.getCount()}")
        }
    }
}

