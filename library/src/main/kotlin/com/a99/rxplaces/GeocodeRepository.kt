package com.a99.rxplaces

import rx.Observable

interface GeocodeRepository {
  fun reverseGeocode(placeId: String) : Observable<GeocodeResult>

  companion object {
    fun create(key: String) : GeocodeRepository {
      return GeocodeRepositoryImpl(GoogleMapsApi.create(), key)
    }
  }
}