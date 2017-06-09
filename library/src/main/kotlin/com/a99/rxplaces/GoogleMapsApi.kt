package com.a99.rxplaces

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Single

interface GoogleMapsApi {
  @GET("/maps/api/place/autocomplete/json")
  fun getPlaceAutocomplete(
      @Query("key") key: String,
      @Query("input") input: String,
      @Query("offset") offset: Int? = null,
      @Query("location") location: String? = null,
      @Query("radius") radius: Int? = null,
      @Query("language") language: String? = null,
      @Query("types") types: String? = null,
      @Query("components") components: String? = null,
      @Query("strictbounds") strictBounds: Boolean? = null
  ): Single<PlaceAutocompleteResponse>

  companion object {
    const val URL = "https://maps.googleapis.com"

    fun create(): GoogleMapsApi {
      val httpLoggingInterceptor = HttpLoggingInterceptor()

      httpLoggingInterceptor.level = when {
        BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
      }

      val client = OkHttpClient.Builder()
          .addInterceptor(httpLoggingInterceptor)
          .build()

      val retrofit = Retrofit.Builder()
          .baseUrl(URL)
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .client(client)
          .build()

      val api = retrofit.create(GoogleMapsApi::class.java)

      return api
    }
  }
}