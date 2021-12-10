/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.github.di

import android.app.Application
import androidx.room.Room
import com.android.example.github.BuildConfig
import com.invotyx.api_builder.ApiBuilder
import com.invotyx.api.GithubAuthService
import com.invotyx.api.GithubService
import com.invotyx.data.db.GithubDb
import com.invotyx.data.db.RepoDao
import com.invotyx.data.db.UserDao
import com.invotyx.envvar.EnvVar
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    /*@Singleton
    @Provides
    fun provideGithubService(
        authenticationInterceptor: AuthenticationInterceptor
    ): GithubService {
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor(authenticationInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(GithubService::class.java)
    }

    @Singleton
    @Provides
    fun provideGithubAuthService(): GithubAuthService {
        return Retrofit.Builder()
            .baseUrl("https://github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubAuthService::class.java)
    }*/

    @Singleton
    @Provides
    fun provideEnvVar(): EnvVar {
        return object : EnvVar {
            override val GITHUB_CLIENT_ID = BuildConfig.GITHUB_CLIENT_ID
            override val GITHUB_CLIENT_SECRET = BuildConfig.GITHUB_CLIENT_SECRET
        }
    }

    @Singleton
    @Provides
    fun provideGithubService(
        apiBuilder: ApiBuilder
    ): GithubService {
        return apiBuilder.buildGithubService(
            baseUrl = "https://api.github.com/",
            loggingLevel = HttpLoggingInterceptor.Level.BODY
        )
    }

    @Singleton
    @Provides
    fun provideGithubAuthService(
        apiBuilder: ApiBuilder
    ): GithubAuthService {
        return apiBuilder.buildGithubAuthService(
            baseUrl = "https://github.com/"
        )
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): GithubDb {
        return Room
            .databaseBuilder(app, GithubDb::class.java, "github.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: GithubDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideRepoDao(db: GithubDb): RepoDao {
        return db.repoDao()
    }
}
