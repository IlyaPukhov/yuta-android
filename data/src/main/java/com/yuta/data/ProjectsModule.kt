package com.yuta.data

import com.yuta.data.network.ApiFactory
import com.yuta.data.network.ProfileApiService
import com.yuta.data.network.ProjectsApiService
import com.yuta.data.repository.ProfileRepositoryImpl
import com.yuta.data.repository.ProjectsRepositoryImpl
import com.yuta.domain.repository.ProfileRepository
import com.yuta.domain.repository.ProjectsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ProjectsModule {

    @Binds
    fun bindRepository(impl: ProjectsRepositoryImpl): ProjectsRepository

    companion object {

        @Provides
        fun provideApiService(): ProjectsApiService {
            return ApiFactory.projectApiService
        }
    }
}