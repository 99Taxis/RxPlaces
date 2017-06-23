package com.a99.rxplaces.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.a99.rxplaces.AutocompleteState
import com.a99.rxplaces.GeocodeRepository
import com.a99.rxplaces.GeocodeResult
import com.a99.rxplaces.Prediction
import com.a99.rxplaces.RxAutocomplete
import com.a99.rxplaces.options.AutocompleteOptions
import com.a99.rxplaces.options.AutocompleteType
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class DemoActivity : AppCompatActivity() {

  val editText by lazy { findViewById(R.id.input) as EditText }
  val loading: View by lazy { findViewById(R.id.loading) }
  val outputContainer by lazy { findViewById(R.id.outputContainer) as LinearLayout }

  val geocodeRepository by lazy { GeocodeRepository.create(BuildConfig.GOOGLE_MAPS_API_KEY) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_demo)

    val rxAutocomplete = RxAutocomplete.create(BuildConfig.GOOGLE_MAPS_API_KEY)

    rxAutocomplete.stateStream()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { autocompleteState ->
          when (autocompleteState) {
            AutocompleteState.QUERYING -> loading.show()
            else -> loading.hide()
          }
        }

    val options = AutocompleteOptions.create {
      types { listOf(AutocompleteType.GEOCODE, AutocompleteType.ESTABLISHMENT) }
      strictBounds()
    }

    rxAutocomplete.observe(editText, options)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { showData(it) }
  }

  private fun showData(predictions: List<Prediction>) {
    outputContainer.removeAllViews()

    predictions
        .map { createTextView(it) }
        .forEach { outputContainer.addView(it) }
  }

  private fun createTextView(prediction: Prediction) = TextView(this)
      .apply {
        text = prediction.description
        setOnClickListener {
          showPlaceDetails(prediction.id)
        }
      }


  private fun showPlaceDetails(placeId: String) {
    geocodeRepository.reverseGeocode(placeId)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe { loading.show() }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { showPlace(it) },
            { },
            { loading.hide() }
        )

  }

  private fun showPlace(geocodeResult: GeocodeResult?) {
    geocodeResult?.let {

    }
  }
}

fun View.show() {
  this.visibility = VISIBLE
}

fun View.hide() {
  this.visibility = INVISIBLE
}
