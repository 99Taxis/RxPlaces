package com.a99.rxplaces

internal fun <T> List<T>.toPipedString(): String? {
  if (isEmpty()) return null

  return map { it.toString() }
      .reduce { accumulator, next -> accumulator.plus("|").plus(next) }
}

internal fun Pair<Double, Double>.formatWithComma() : String {
  return "$first,$second"
}
