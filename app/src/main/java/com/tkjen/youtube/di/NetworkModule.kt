package com.tkjen.youtube.di

import android.util.Log
import com.tkjen.youtube.BuildConfig
import com.tkjen.youtube.data.api.YoutubeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message) // Log với tag "OkHttp"
        }
        // Chỉ nên dùng Level.BODY khi debug, vì nó có thể log thông tin nhạy cảm.
        // Trong môi trường production, bạn có thể muốn dùng Level.BASIC hoặc Level.NONE.
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE) // Hoặc Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // Bạn có thể thêm các cấu hình khác cho OkHttpClient ở đây nếu cần
            // .connectTimeout(30, TimeUnit.SECONDS)
            // .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // OkHttpClient được inject từ hàm trên
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Sử dụng OkHttpClient đã được cấu hình
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideYoutubeApiService(retrofit: Retrofit): YoutubeApiService {
        return retrofit.create(YoutubeApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("youtube_api_key")
    fun provideYoutubeApiKey(): String {
        val apiKey = BuildConfig.YOUTUBE_API_KEY
        Log.d("NetworkModule", "Provided API Key (first 5 chars): ${apiKey.take(5)}...") // Log một phần key

        // Kiểm tra kỹ hơn cho API Key
        if (apiKey.isBlank() ||
            apiKey == "null" ||
            apiKey.equals("YOUR_ACTUAL_API_KEY", ignoreCase = true) || // Thêm kiểm tra placeholder phổ biến
            apiKey.equals("YOUR_API_KEY_HERE", ignoreCase = true) ||
            apiKey.length < 30) { // API key của Google thường dài hơn nhiều
            val errorMessage = "API Key is invalid, empty, null, a placeholder, or too short! Please check your local.properties."
            Log.e("NetworkModule", errorMessage)
            // Cân nhắc throw một Exception ở đây để ứng dụng dừng lại nếu API Key không hợp lệ,
            // thay vì tiếp tục và gây lỗi 403 khó hiểu hơn.
            // throw IllegalArgumentException(errorMessage)
        }
        return apiKey
    }
}