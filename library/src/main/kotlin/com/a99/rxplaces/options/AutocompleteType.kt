package com.a99.rxplaces.options

enum class AutocompleteType {
  GEOCODE, ADDRESS, ESTABLISHMENT, REGIONS, CITIES;

  override fun toString() : String {
    return when (this) {
      REGIONS -> "(regions)"
      CITIES -> "(cities)"
      else -> this.name.toLowerCase()
    }
  }
}
