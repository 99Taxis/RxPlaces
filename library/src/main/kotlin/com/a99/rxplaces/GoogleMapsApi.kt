package com.a99.rxplaces

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Single

internal interface GoogleMapsApi {
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

  @GET("/maps/api/geocode/json")
  fun getReverseGeocode(
      @Query("key") key: String,
      @Query("place_id") placeId: String
  ): Single<GeocodeResponse>

  companion object {
    const val URL = "https://maps.googleapis.com"

    const val OK = "OK"
    const val ZERO_RESULTS = "ZERO_RESULTS"
    const val OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT"
    const val REQUEST_DENIED = "REQUEST_DENIED"
    const val INVALID_REQUEST = "INVALID_REQUEST"
    const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

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