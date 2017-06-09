package com.a99.rxplaces.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.a99.rxplaces.AutocompleteState
import com.a99.rxplaces.Prediction
import com.a99.rxplaces.RxAutocomplete
import rx.android.schedulers.AndroidSchedulers

class DemoActivity : AppCompatActivity() {

  val editText by lazy { findViewById(R.id.input) as EditText }
  val loading: View by lazy { findViewById(R.id.loading) }
  val outputContainer by lazy { findViewById(R.id.outputContainer) as LinearLayout }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_demo)

    val rxAutocomplete = RxAutocomplete.create(BuildConfig.GOOGLE_MAPS_API_KEY)

    rxAutocomplete.stateStream()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { autocompleteState ->
          loading.visibility = when (autocompleteState) {
            AutocompleteState.QUERYING -> VISIBLE
            else -> GONE
          }
        }

    rxAutocomplete.observe(editText)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { showData(it) }
  }

  private fun showData(predictions: List<Prediction>) {
    outputContainer.removeAllViews()

    predictions
        .map { createTextView(it) }
        .forEach { outputContainer.addView(it) }
  }

  private fun createTextView(it: Prediction): TextView {
    val textView = TextView(this)
    textView.text = it.description
    return textView
  }
}
