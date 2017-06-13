package com.a99.rxplaces

import com.a99.rxplaces.options.AutocompleteOptions
import rx.Single

interface PlacesAutocompleteRepository {
  fun query(input: String, options: AutocompleteOptions) : Single<List<Prediction>>
}