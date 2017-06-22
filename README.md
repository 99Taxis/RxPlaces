# RxPlaces [![Build Status](https://travis-ci.org/99Taxis/RxPlaces.svg?branch=master)](https://travis-ci.org/99Taxis/RxPlaces)  [ ![Download](https://api.bintray.com/packages/99/android/rxplaces/images/download.svg) ](https://bintray.com/99/android/rxplaces/_latestVersion)
A reactive approach for the Google Maps Webservice API

## Current features:
- Places Autocomplete
- Reverse Geocoding from given `place_id`

## Download

```groovy
compile 'com.a99.rxplaces:library:0.1.0-alpha'
```

## Proguard Rules

Add the following line to your `proguard-rules.pro`

```
-keep class com.a99.rxplaces.** { *; }
```


## Places Autocomplete

### Initialization

You should use the function `create(GOOGLE_MAPS_API_AUTOCOMPLETE_KEY)`, where `GOOGLE_MAPS_API_AUTOCOMPLETE_KEY` is the generated key from the Google developer console:

```kotlin
val rxAutocomplete = RxAutocomplete.create(GOOGLE_MAPS_API_AUTOCOMPLETE_KEY)
```

### Usage
You can observe an `EditText`:

```kotlin
rxAutocomplete.observe(editText, options)
```

You can also observe any `String` data source:

```kotlin
Observable<String> dataSource = Observable.just("place");

rxAutocomplete.observe(dataSource, options)
```

### Options Parameters

You can customize any value provided from the [Google Maps Place Autocomplete webservice](https://developers.google.com/places/web-service/autocomplete):

```kotlin
val options = AutocompleteOptions.create {
  offset { MIN_OFFSET }
  location { latLng.latitude to latLng.longitude }
  radius { RADIUS_IN_METERS }
  language { locale.language }
  types { AutocompleteType.CITIES } //Check the AutocompleteType class to get all the possible types
  components { listOf(DEFAULT_COUNTRY) }
  strictBounds { true }
}
```

### Listening events

You can use the `stateStream()` to handle the provided events from the API:

```kotlin
rxAutocomplete.stateStream()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe {
      when(it) {
        AutocompleteState.QUERYING -> view.showProgress()
        AutocompleteState.FAILURE -> view.showError()
        AutocompleteState.SUCCESS -> view.hideProgress()
      }
    }
```

### API Defaults

- The default min key stroke is `3`
- The default query interval is `2 seconds`


## Reverse Geocoding

To get the latitude and longitude from a given place, you can use the `GeocodeRepository`

### Initialization

You should use the function `create(GOOGLE_MAPS_API_GEOCODE_KEY)`, where `GOOGLE_MAPS_API_GEOCODE_KEY` is the generated key from the Google developer console:

```kotlin
val geocodeRepository = GeocodeRepository.create(BuildConfig.GOOGLE_MAPS_API_GEOCODE_KEY)
```

### Usage

You can use the method `reverseGeocode(placeId)`:

```kotlin
geocodeRepository.reverseGeocode(placeId)
    .subscribeOn(Schedulers.io())
    .take(1)
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(
        { view.dispatchGeocodeResult(it)},
        { view.showError() }
    )
```

### Accessing the provided results

You can easily get the result information through the API given functions, example:

```kotlin
private fun createResult(result: GeocodeResult) = Result(
  latitude = result.geometry.location.lat,
  longitude = result.geometry.location.lng,
  formattedAddress = result.formattedAddress,
  street = result.getStreetName(),
  number = result.getStreetNumber(),
  city = result.getCity(),
  neighborhood = result.getNeighborhood(),
  postalCode = result.getPostalCode(),
)
```

Note: the API will return the `long name` attribute from the response.

## Demo
Look the [demo folder](https://github.com/99Taxis/RxPlaces/tree/master/demo)


## Tests
`./gradlew test`


## Contributing

Don't be shy, send a Pull Request! Here is how:

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

License
-------

    Copyright (C) 2017 99

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
