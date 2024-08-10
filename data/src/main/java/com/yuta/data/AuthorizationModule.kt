package com.yuta.data

import com.yuta.data.network.ApiFactory
import com.yuta.data.network.AuthorizationApiService
import com.yuta.data.repository.AuthorizationRepositoryImpl
import com.yuta.domain.repository.AuthorizationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface AuthorizationModule {

    @Binds
    fun bindRepository(impl: AuthorizationRepositoryImpl): AuthorizationRepository

    companion object {

        @Provides
        fun provideApiService(): AuthorizationApiService {
            return ApiFactory.authorizationApiService
        }
    }
}