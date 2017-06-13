package com.a99.rxplaces

import android.support.annotation.VisibleForTesting
import android.util.Log
import android.widget.TextView
import com.a99.rxplaces.options.AutocompleteOptions
import rx.Observable
import rx.Scheduler
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RxAutocomplete internal constructor(
    val scheduler: Scheduler,
    val repository: PlacesAutocompleteRepository,
    val logger: (String, String) -> Unit = { _, _ -> }) {

  var minKeyStroke: Int = 3
  var queryInterval: Pair<Long, TimeUnit> = 2L to TimeUnit.SECONDS

  private val autocompleteStateSubject = PublishSubject.create<AutocompleteState>()

  fun stateStream(): Observable<AutocompleteState> {
    return autocompleteStateSubject.onBackpressureLatest()
  }

  fun observe(
      textView: TextView,
      options: AutocompleteOptions = AutocompleteOptions.default()): Observable<List<Prediction>> {

    val dataSource = RxTextView.textChanges(textView)
        .map { it.toString() }

    return observe(dataSource, options)
  }

  fun observe(
      dataSource: Observable<String>,
      options: AutocompleteOptions = AutocompleteOptions.default()): Observable<List<Prediction>> {

    return dataSource
        .observeOn(scheduler)
        .filter { it.length > minKeyStroke }
        .doOnNext { logger("RxAutocomplete", "Received: $it") }
        .buffer(queryInterval.first, queryInterval.second)
        .filter { it.isNotEmpty() }
        .map { it.last() }
        .concatMap { input ->
          repository.query(input, options)
              .doOnSubscribe { logger("RxAutocomplete", "START QUERY: $input") }
              .doOnSubscribe { autocompleteStateSubject.onNext(AutocompleteState.QUERYING) }
              .doOnSuccess { autocompleteStateSubject.onNext(AutocompleteState.SUCCESS) }
              .doOnError { autocompleteStateSubject.onNext(AutocompleteState.FAILURE) }
              .toObservable()
              .onErrorResumeNext { Observable.empty() }
        }
  }

  companion object {
    fun create(
        apiKey: String,
        scheduler: Scheduler = Schedulers.io(),
        logger: (String, String) -> Unit = { tag, message -> Log.d(tag, message) }
    ): RxAutocomplete {
      val repository = PlacesAutocompleteRepositoryImpl(apiKey, GoogleMapsApi.create())
      return RxAutocomplete(scheduler, repository, logger)
    }

    @VisibleForTesting
    internal fun create(
        scheduler: Scheduler,
        repository: PlacesAutocompleteRepository,
        logger: (String, String) -> Unit
    ): RxAutocomplete {
      return RxAutocomplete(scheduler, repository, logger)
    }
  }
}
