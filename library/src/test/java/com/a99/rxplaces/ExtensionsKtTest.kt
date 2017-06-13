package com.a99.rxplaces

import android.location.Location
import com.a99.rxplaces.options.AutocompleteType
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.Random

class ExtensionsKtTest {
  @Test
  fun toPipedString_emptyList_shouldReturnNull() {
    // given
    val components = emptyList<String>()

    // when
    val formatted = components.toPipedString()

    // then
    assert(formatted == null)
  }

  @Test
  fun toPipedString_oneElement_shouldReturnItself() {
    // given
    val components = listOf("country:br")

    // when
    val formatted = components.toPipedString()

    // then
    assertLastCharacterIsNotPipe(formatted!!)
    assertThat(formatted, `is`("country:br"))
  }

  @Test
  fun toPipedString_twoElement_shouldAddPipeBetweenElements() {
    // given
    val types = listOf(AutocompleteType.CITIES, AutocompleteType.ESTABLISHMENT)

    // when
    val formatted = types.toPipedString()

    // then
    assertLastCharacterIsNotPipe(formatted!!)
    assertThat(formatted, `is`("(cities)|establishment"))
  }

  @Test
  fun toPipedString_randomElements_shouldAddPipeBetweenElements() {
    // given
    val limit = Random().nextInt(10000)
    val range = 1..limit
    val components = range.map { "country:$it" }

    // when
    val formatted = components.toPipedString()

    // then
    assertLastCharacterIsNotPipe(formatted!!)
    assertThat(formatted.split("|").size, `is`(limit))
    assertThat(formatted.split("|").last(), `is`("country:$limit"))
  }

  @Test
  fun formatLocation() {
    // given
    val location = mock<Location> {
      on { latitude }.doReturn(10.0)
      on { longitude }.doReturn(-20.0)
    }

    // when
    val formatted = location.formatWithComma()

    // then
    assertThat(formatted, `is`("10.0,-20.0"))
  }

  private fun assertLastCharacterIsNotPipe(formatted: String) {
    assertThat(formatted.last().toString(), not(`is`("|")))
  }
}