package com.yuta.app

import android.app.Application
import com.yuta.app.di.ApplicationComponent
import com.yuta.app.di.DaggerApplicationComponent

class YutaApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}
