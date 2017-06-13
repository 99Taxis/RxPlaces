package com.a99.rxplaces

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Single
import rx.observers.TestSubscriber
import rx.subjects.TestSubject
import java.util.Random
import java.util.concurrent.TimeUnit

class RxAutocompleteStressTest {
  @Rule @JvmField val testSchedulerRule = TestSchedulerRule()
  val testScheduler = testSchedulerRule.testScheduler
  val repository = mock<PlacesAutocompleteRepository> {
    on {
      query(any(), any())
    } doReturn Single.fromCallable { listOf<Prediction>() }
  }

  @Test
  fun manyInputs_samePace() {
    // given
    val testSubscriber = TestSubscriber<Any>()
    val words = listOf("avenida brasil", "rua alvorada", "avenida rio branco")

    val from = simulateTyping(Observable.from(words))
        .zipWith(Observable.interval(100, TimeUnit.MILLISECONDS), { word, _ -> word })

    val rxAutocomplete = createRxAutoComplete(repository)

    rxAutocomplete.observe(from)
        .subscribe(testSubscriber)

    // when
    shiftTime(10, TimeUnit.SECONDS)

    // then
    testSubscriber.assertNoErrors()
    testSubscriber.assertValueCount(3)

    verify(repository, times(3)).query(any(), any())
  }

  @Test
  fun manyInputs_differentPace_oneQuery() {
    // given
    val testSubscriber = TestSubscriber<Any>()
    val testSubject = TestSubject.create<String>(testScheduler)
    val rxAutocomplete = createRxAutoComplete(repository)

    rxAutocomplete.observe(testSubject)
        .subscribe(testSubscriber)

    // when
    testSubject.onNext("aven")
    shiftTime(300, TimeUnit.MILLISECONDS)
    verify(repository, never()).query(any(), any())

    testSubject.onNext("aveni")
    shiftTime(500, TimeUnit.MILLISECONDS)
    verify(repository, never()).query(any(), any())

    testSubject.onNext("avenida")
    shiftTime(600, TimeUnit.MILLISECONDS)
    verify(repository, never()).query(any(), any())

    testSubject.onNext("avenida bra")
    shiftTime(600, TimeUnit.MILLISECONDS)
    verify(repository).query(any(), any())

    // then
    testSubscriber.assertNoErrors()
    testSubscriber.assertValueCount(1)
  }

  @Test
  fun manyInputs_differentPace_manyQueries() {
    // given
    val testSubscriber = TestSubscriber<Any>()
    val testSubject = TestSubject.create<String>(testScheduler)
    val rxAutocomplete = createRxAutoComplete(repository)

    rxAutocomplete.observe(testSubject)
        .subscribe(testSubscriber)

    // when
    testSubject.onNext("aven")
    shiftTime(1, TimeUnit.SECONDS)
    verify(repository, never()).query(any(), any())

    testSubject.onNext("aveni")
    shiftTime(700, TimeUnit.MILLISECONDS)
    verify(repository, never()).query(any(), any())

    testSubject.onNext("avenida")
    shiftTime(500, TimeUnit.MILLISECONDS)
    verify(repository).query(any(), any())

    testSubject.onNext("avenida bra")
    shiftTime(2, TimeUnit.SECONDS)
    verify(repository, times(2)).query(any(), any())

    // then
    testSubscriber.assertNoErrors()
    testSubscriber.assertValueCount(2)
  }

  @Test
  fun randomInputs_inShortRandomInterval() {
    val testSubscriber = TestSubscriber<Any>()
    val randomInterval = { Random().nextInt(1000).toLong() }

    val randomInputs = Observable.range(1, Int.MAX_VALUE)
        .map { "Input $it" }
        .delay { Observable.interval(randomInterval(), TimeUnit.MILLISECONDS) }

    val repository = mock<PlacesAutocompleteRepository> {
      on {
        query(any(), any())
      } doReturn Single.fromCallable { listOf<Prediction>() }
    }

    val rxAutocomplete = createRxAutoComplete(repository)

    rxAutocomplete.observe(randomInputs)
        .subscribe(testSubscriber)

    // when
    shiftTime(10, TimeUnit.SECONDS)

    // then
    testSubscriber.assertNoErrors()
    testSubscriber.assertValueCount(5)

    verify(repository, times(5)).query(any(), any())
  }

  private fun createRxAutoComplete(repository: PlacesAutocompleteRepository) = RxAutocomplete.create(testScheduler, repository, testSchedulerRule::logger)

  private fun shiftTime(interval: Long, timeUnit: TimeUnit) {
    testSchedulerRule.testScheduler.advanceTimeBy(interval, timeUnit)
  }

  private fun simulateTyping(words: Observable<String>): Observable<String> {
    return words.map { it.toCharArray() }
        .concatMap { charArray ->
          Observable.from(charArray.toList())
              .map { it.toString() }
              .scan { accumulator: String, next: String -> accumulator + next }
        }
  }
}