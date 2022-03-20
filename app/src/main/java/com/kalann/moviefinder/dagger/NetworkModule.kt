package com.kalann.moviefinder.dagger

import com.kalann.moviefinder.api.MFinService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build();
    }
    @ApplicationScope
    @Provides
    fun provideRetrofitClient(moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.themoviedb.org/")
            .build()
    }
    @ApplicationScope
    @Provides
    fun provideMFinService(retrofit: Retrofit): MFinService {
        // Whenever Dagger needs to provide an instance of type LoginRetrofitService,
        // this code (the one inside the @Provides method) is run.
        return retrofit
            .create(MFinService::class.java)
    }
}
