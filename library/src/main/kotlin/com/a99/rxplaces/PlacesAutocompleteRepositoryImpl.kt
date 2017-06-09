package com.a99.rxplaces

import rx.Single

internal class PlacesAutocompleteRepositoryImpl
constructor(val apiKey: String, val googleMapsApi: GoogleMapsApi) : PlacesAutocompleteRepository {

  override fun query(
      input: String,
      types: Array<String>,
      components: Array<String>
  ): Single<List<Prediction>> {
    return googleMapsApi.getPlaceAutocomplete(apiKey, input)
        .flatMap { (status, predictions) ->
          if (status == STATUS_OK) {
            return@flatMap Single.fromCallable { predictions }
          } else {
            return@flatMap Single.error<List<Prediction>>(Exception("Failure with status $status"))
          }
        }
  }

  companion object {
    private const val STATUS_OK = "OK"
  }
}
