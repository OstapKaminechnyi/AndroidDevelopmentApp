package iot.ostapkmn.app.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    private const val URL = "https://formidable-card-258617.appspot.com/api/"

    val getClient: ApiInterface
        get() {
            val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build()
            val retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                            .setLenient()
                            .create()))
                    .build()
            return retrofit.create(ApiInterface::class.java)
        }
}