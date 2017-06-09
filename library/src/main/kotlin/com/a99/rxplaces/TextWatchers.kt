package com.a99.rxplaces

import android.text.Editable
import android.text.TextWatcher
import rx.Subscriber

internal class TextWatchers {
  companion object {
    fun from(subscriber: Subscriber<in CharSequence>) : TextWatcher {
      return object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          if (!subscriber.isUnsubscribed) {
            subscriber.onNext(s)
          }
        }
      }
    }
  }
}