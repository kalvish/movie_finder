package com.kalann.moviefinder

import android.app.Application
import com.kalann.moviefinder.dagger.AppComponent
import com.kalann.moviefinder.dagger.DaggerAppComponent

class MFinApplication : Application(){
    val daggerAppComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
//    fun initializeComponent(): AppComponent {
//        return DaggerAppComponent.factory().create(applicationContext)
//    }
//    val daggerAppComponent = DaggerAppComponent.create()
}