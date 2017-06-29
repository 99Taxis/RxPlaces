package com.a99.rxplaces

import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.observers.SerializedSubscriber
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit

class AutocompleteBufferOperator<T>(
    scheduler: Scheduler = Schedulers.computation(),
    val timespan: Pair<Long, TimeUnit>
) : Observable.Operator<T, T> {
  private val inner = scheduler.createWorker()
  private var emitterSubscription = Subscriptions.empty()
  private var scheduledCompletion: (() -> Unit)? = null

  override fun call(upstreamSubscriber: Subscriber<in T>): Subscriber<in T> {
    return AutocompleteBufferOperatorSubscriber(upstreamSubscriber)
  }

  private inner class AutocompleteBufferOperatorSubscriber<T>(
      val upstreamSubscriber: Subscriber<T>
  ) : Subscriber<T>() {

    override fun onNext(upstreamData: T) {
      if (upstreamSubscriber.isUnsubscribed) return
      val serialized = SerializedSubscriber<T>(upstreamSubscriber)
      cancelPreviousEmission()
      scheduleNextEmission(serialized, upstreamData)
    }

    override fun onCompleted() {
      if (upstreamSubscriber.isUnsubscribed) return
      if (emitterSubscription.isUnsubscribed) {
        upstreamSubscriber.onCompleted()
      } else {
        scheduledCompletion = { upstreamSubscriber.onCompleted() }
      }
    }

    override fun onError(e: Throwable?) {
      if (upstreamSubscriber.isUnsubscribed) return
      upstreamSubscriber.onError(e)
    }

    private fun scheduleNextEmission(serialized: SerializedSubscriber<T>, t: T) {
      emitterSubscription = inner.schedule(
          {
            serialized.onNext(t)
            scheduledCompletion?.invoke()
          },
          timespan.interval(), timespan.timeUnit())
    }

    private fun cancelPreviousEmission() {
      emitterSubscription.unsubscribe()
    }
  }
}