package com.tkjen.youtube.di

import com.tkjen.youtube.BuildConfig
import com.tkjen.youtube.data.api.YoutubeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideYoutubeApiService(retrofit: Retrofit): YoutubeApiService =
        retrofit.create(YoutubeApiService::class.java)

    @Provides
    @Singleton
    @Named("youtube_api_key")
    fun provideYoutubeApiKey(): String = BuildConfig.YOUTUBE_API_KEY

}
