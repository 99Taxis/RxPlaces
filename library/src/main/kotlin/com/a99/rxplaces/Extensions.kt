package com.a99.rxplaces

import java.util.concurrent.TimeUnit

internal fun <T> List<T>.toPipedString(): String? {
  if (isEmpty()) return null

  return this.joinToString("|")
}

internal fun Pair<Double, Double>.formatWithComma(): String {
  return "$first,$second"
}

internal fun Pair<Long, TimeUnit>.interval() = first
internal fun Pair<Long, TimeUnit>.timeUnit() = second
internal fun Pair<Long, TimeUnit>.intervalInMillis() = timeUnit().toMillis(interval())