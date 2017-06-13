package com.a99.rxplaces

import junit.framework.Assert.assertEquals
import org.junit.Test

class GeocodeResultTest {
  @Test
  fun getAddressComponentLongName_shouldReturnComponents() {
    val geocodeResult = getFakeResponse().results!!.first()

    assertEquals("Rua Joaquim Floriano", geocodeResult.getStreetName())
    assertEquals("254", geocodeResult.getStreetNumber())
    assertEquals("Itaim Bibi", geocodeResult.getSublocality())
    assertEquals("São Paulo", geocodeResult.getCity())
    assertEquals("São Paulo", geocodeResult.getState())
    assertEquals("Brasil", geocodeResult.getCountry())
    assertEquals("04534-001", geocodeResult.getPostalCode())
  }
}


