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

import android.app.Application
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.obaralic.shade.model.database.AppDatabase
import com.obaralic.shade.model.database.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * A module for application scope specific dependencies which require
 * {@link Context} or {@link android.app.Application} to create.
 * Use this module to provide dependency creation for
 * database, repo, apis, web etc.
 */

@Module
class AppModule {

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideLocationManager(application: Application): LocationManager =
        application.getSystemService(LOCATION_SERVICE) as LocationManager

    /*
     * The method returns the Gson object
     * */
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    /**
     * Provides injection of the UserDao objects.
     */
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    /**
     * Provides injection of the AppDatabase objects.
     * Since the dao creation is dependent on database,
     * database object injection needs to be provided as well.
     */
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext, AppDatabase::class.java,  AppDatabase.NAME)
            .addCallback(AppDatabase.seedDatabaseCallback(application))
            .build()
    }

}