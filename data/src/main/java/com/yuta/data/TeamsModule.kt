package com.yuta.data

import com.yuta.data.network.ApiFactory
import com.yuta.data.network.TeamsApiService
import com.yuta.data.repository.TeamsRepositoryImpl
import com.yuta.domain.repository.TeamsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface TeamsModule {

    @Binds
    fun bindRepository(impl: TeamsRepositoryImpl): TeamsRepository

    companion object {

        @Provides
        fun provideApiService(): TeamsApiService {
            return ApiFactory.teamsApiService
        }
    }
}