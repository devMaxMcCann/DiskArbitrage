package com.CDScanner.discogs

import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface DiscogsApi {
    @GET("database/search")
    suspend fun searchByBarcode(
        @Query("barcode") barcode: String,
        @Query("per_page") perPage: Int = 100
    ): SearchResponse

    @GET("releases/{id}")
    suspend fun getRelease(@Path("id") id: Int): ReleaseResponse

    @GET("marketplace/stats/{id}")
    suspend fun getStats(@Path("id") id: Int): StatsResponse

    companion object {
        fun create(token: String): DiscogsApi {
            val client = okhttp3.OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Discogs token=$token")
                        .addHeader("User-Agent", "FieldScanner/1.0")
                        .build()
                    chain.proceed(newRequest)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api.discogs.com/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(DiscogsApi::class.java)
        }
    }
}