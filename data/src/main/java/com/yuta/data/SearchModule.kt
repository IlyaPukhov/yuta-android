package com.yuta.data

import com.yuta.data.network.ApiFactory
import com.yuta.data.network.SearchApiService
import com.yuta.data.repository.SearchRepositoryImpl
import com.yuta.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface SearchModule {

    @Binds
    fun bindRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @Provides
        fun provideApiService(): SearchApiService {
            return ApiFactory.searchApiService
        }
    }
}