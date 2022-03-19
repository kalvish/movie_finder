package com.kalann.moviefinder

import android.app.Application
import com.kalann.moviefinder.dagger.DaggerAppComponent

class MFinApplication : Application(){
    val daggerAppComponent = DaggerAppComponent.create();
}