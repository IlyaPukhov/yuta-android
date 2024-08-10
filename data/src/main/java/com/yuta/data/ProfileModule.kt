package com.yuta.data

import com.yuta.data.network.ApiFactory
import com.yuta.data.network.ProfileApiService
import com.yuta.data.repository.ProfileRepositoryImpl
import com.yuta.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ProfileModule {

    @Binds
    fun bindRepository(impl: ProfileRepositoryImpl): ProfileRepository

    companion object {

        @Provides
        fun provideApiService(): ProfileApiService {
            return ApiFactory.profileApiService
        }
    }
}