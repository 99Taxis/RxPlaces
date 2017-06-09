package com.a99.rxplaces

import rx.Single

interface PlacesAutocompleteRepository {
  fun query(input: String, types: Array<String>, components: Array<String>) : Single<List<Prediction>>
}