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

import android.content.Context
import android.location.LocationManager
import com.obaralic.shade.di.component.DaggerAppComponent
import com.obaralic.shade.model.database.AppDatabase
import com.obaralic.shade.model.database.UserDao
import com.obaralic.shade.util.ioThread
import com.obaralic.shade.util.log.CrashReportingTree
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject


class App : DaggerApplication() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var userDao: UserDao

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initDatabase()
    }

    private fun initDatabase() {
        ioThread {
            // Just accessing database so that it can be created.
            val lUserDao = database.userDao()
            Timber.d("Users no: ${userDao.getCount()} / ${lUserDao.getCount()}")
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        else Timber.plant(CrashReportingTree())
    }
}

