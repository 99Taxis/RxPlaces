package com.a99.rxplaces

import com.a99.rxplaces.options.AutocompleteType
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class AutocompleteTypeTest {
  @Test
  fun toString_cities() {
    // when
    val cities = AutocompleteType.CITIES.toString()

    // then
    assertThat(cities, `is`("(cities)"))
  }

  @Test
  fun toString_regions() {
    // when
    val regions = AutocompleteType.REGIONS.toString()

    // then
    assertThat(regions, `is`("(regions)"))
  }

  @Test
  fun toString_establishment() {
    // when
    val establishment = AutocompleteType.ESTABLISHMENT.toString()

    // then
    assertThat(establishment, `is`("establishment"))
  }

  @Test
  fun toString_address() {
    // when
    val address = AutocompleteType.ADDRESS.toString()

    // then
    assertThat(address, `is`("address"))
  }

  @Test
  fun toString_geocode() {
    // when
    val geocode = AutocompleteType.GEOCODE.toString()

    // then
    assertThat(geocode, `is`("geocode"))
  }
}