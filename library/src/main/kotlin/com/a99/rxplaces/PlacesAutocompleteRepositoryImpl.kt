package com.a99.rxplaces

import com.a99.rxplaces.options.AutocompleteOptions
import rx.Single

internal class PlacesAutocompleteRepositoryImpl
constructor(val apiKey: String, val googleMapsApi: GoogleMapsApi) : PlacesAutocompleteRepository {

  override fun query(
      input: String,
      options: AutocompleteOptions): Single<List<Prediction>> {

    val single = createAutocompleteQuerySingle(input, options)
        .flatMap { flattenPredictions(it) }

    return single
  }

  private fun createAutocompleteQuerySingle(
      input: String,
      options: AutocompleteOptions): Single<PlaceAutocompleteResponse> {

    return googleMapsApi.getPlaceAutocomplete(
        key = apiKey,
        input = input,
        offset = options.offset,
        location = options.location?.formatWithComma(),
        radius = options.radius,
        language = options.language,
        types = options.types.toPipedString(),
        components = options.components.toPipedString(),
        strictBounds = options.strictBounds
    )
  }

  private fun flattenPredictions(response: PlaceAutocompleteResponse): Single<List<Prediction>>? {
    if (response.status == STATUS_OK) {
      return Single.fromCallable { response.predictions }
    } else {
      return Single.error(Exception("Failure with status ${response.status}"))
    }
  }

  companion object {
    private const val STATUS_OK = "OK"
  }
}
