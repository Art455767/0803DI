package ru.netology.nmedia.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.api.Api
import ru.netology.nmedia.api.ApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Api.service
    }
}