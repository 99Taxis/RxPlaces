package com.a99.rxplaces

import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import rx.subjects.TestSubject
import java.util.concurrent.TimeUnit

class AutocompleteBufferOperatorTest {
  @Rule @JvmField val testSchedulerRule = TestSchedulerRule()
  val testScheduler = testSchedulerRule.testScheduler
  val logger = testSchedulerRule::logger
  val shiftTime = testScheduler::advanceTimeBy
  val timespan = 1L to TimeUnit.SECONDS
  val operator = AutocompleteBufferOperator<String>(testScheduler, timespan)

  @Test
  fun testCommonEmission_shouldEmitTwo() {
    // given
    val testSubscriber = TestSubscriber<Any>()
    val testSubject = TestSubject.create<String>(testScheduler)

    testSubject
        .doOnNext { logger("Received", it) }
        .lift(operator)
        .doOnNext { logger("Emitted", it) }
        .subscribe(testSubscriber)

    // when
    testSubject.onNext("aven")
    shiftTime(timespan.interval(), timespan.timeUnit())

    testSubject.onNext("aveni")
    shiftTime(700, TimeUnit.MILLISECONDS)

    testSubject.onNext("avenida")
    shiftTime(500, TimeUnit.MILLISECONDS)

    testSubject.onNext("avenida bra")
    shiftTime(timespan.interval().times(2), timespan.timeUnit())

    // then
    testSubscriber.assertNoErrors()
    testSubscriber.assertValueCount(2)
    testSubscriber.assertValues("aven", "avenida bra")
  }

  @Test
  fun testSimulateFastTyping_shouldEmitOnlyLastWord() {
    // given
    val testSubscriber = TestSubscriber<Any>()
    val words = listOf("avenida brasil", "rua alvorada", "avenida rio branco")

    val fastTyping = simulateTyping(Observable.from(words))
        .zipWith(Observable.interval(100, TimeUnit.MILLISECONDS), { word, _ -> word })


    // when
    fastTyping
        .doOnNext { logger("Received", it) }
        .lift(operator)
        .doOnNext { logger("Emitted", it) }
        .subscribe(testSubscriber)

    shiftTime(1, TimeUnit.MINUTES)

    // then
    testSubscriber.assertValueCount(1)
    testSubscriber.assertValues("avenida rio branco")
    testSubscriber.assertCompleted()
  }
}