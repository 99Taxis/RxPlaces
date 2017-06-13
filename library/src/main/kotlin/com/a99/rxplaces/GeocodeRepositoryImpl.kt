package com.a99.rxplaces

import com.a99.rxplaces.GoogleMapsApi.Companion.INVALID_REQUEST
import com.a99.rxplaces.GoogleMapsApi.Companion.OK
import com.a99.rxplaces.GoogleMapsApi.Companion.OVER_QUERY_LIMIT
import com.a99.rxplaces.GoogleMapsApi.Companion.REQUEST_DENIED
import com.a99.rxplaces.GoogleMapsApi.Companion.UNKNOWN_ERROR
import com.a99.rxplaces.GoogleMapsApi.Companion.ZERO_RESULTS
import rx.Observable


internal class GeocodeRepositoryImpl (val api: GoogleMapsApi, val key: String) : GeocodeRepository {
  override fun reverseGeocode(placeId: String): Observable<GeocodeResult> {
    return api.getReverseGeocode(key, placeId)
        .flatMapObservable {
          when (it.status) {
            OK -> Observable.from(it.results)
            ZERO_RESULTS -> Observable.empty()
            OVER_QUERY_LIMIT -> Observable.error(OverQueryLimitException(FAILURE_MESSAGE))
            REQUEST_DENIED -> Observable.error(RequestDeniedException(FAILURE_MESSAGE))
            INVALID_REQUEST -> Observable.error(InvalidRequestException(FAILURE_MESSAGE))
            UNKNOWN_ERROR -> Observable.error(UnknownErrorException(FAILURE_MESSAGE))
            else -> Observable.error(Exception(FAILURE_MESSAGE.plus(" Status: ${it.status}")))
          }
        }
  }

  companion object {
    private const val FAILURE_MESSAGE = "Can't perform reverse geocode."
  }
}