package com.a99.rxplaces

import android.widget.TextView
import rx.Observable
import rx.Subscriber
import rx.android.MainThreadSubscription
import rx.android.MainThreadSubscription.verifyMainThread

internal class RxTextView {

  companion object {
    fun textChanges(textView: TextView): Observable<CharSequence> {
      val observable = Observable.create<CharSequence> { subscriber ->
        try {
          verifyMainThread()

          val watcher = TextWatchers.from(subscriber)

          textView.addTextChangedListener(watcher)

          subscriber.onUnsubscribe { textView.addTextChangedListener(watcher) }
        } catch (t: Throwable) {
          subscriber.onError(t)
        }
      }

      return observable.onBackpressureLatest()
    }

    infix fun <T> Subscriber<T>.onUnsubscribe(function: () -> Unit) {
      add(object : MainThreadSubscription() {
        override fun onUnsubscribe() {
          function()
        }
      })
    }
  }
}