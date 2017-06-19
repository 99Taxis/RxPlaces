package com.a99.rxplaces

internal fun <T> List<T>.toPipedString(): String? {
  if (isEmpty()) return null

  return this.joinToString("|")
}

internal fun Pair<Double, Double>.formatWithComma(): String {
  return "$first,$second"
}
