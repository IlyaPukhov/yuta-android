package com.yuta.app.di

import com.yuta.app.MainActivity
import com.yuta.authorization.ui.AuthorizationActivity
import com.yuta.data.AuthorizationModule
import com.yuta.data.ProfileModule
import com.yuta.data.ProjectsModule
import com.yuta.data.SearchModule
import com.yuta.data.TeamsModule
import dagger.Component

@Component(
    modules = [
        AuthorizationModule::class,
        ProfileModule::class,
        ProjectsModule::class,
        SearchModule::class,
        TeamsModule::class
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(authorizationActivity: AuthorizationActivity)

    @Component.Factory
    interface Factory {
        fun create(): ApplicationComponent
    }
}
