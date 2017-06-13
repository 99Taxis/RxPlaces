package com.a99.rxplaces

import android.location.Location

internal fun <T> List<T>.toPipedString(): String? {
  if (isEmpty()) return null

  return map { it.toString() }
      .reduce { accumulator, next -> accumulator.plus("|").plus(next) }
}

internal fun Location.formatWithComma() : String {
  return "$latitude,$longitude"
}