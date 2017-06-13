package com.a99.rxplaces.options

data class AutocompleteOptions (
    val offset: Int? = null,
    val location: Pair<Double, Double>? = null,
    val radius: Int? = null,
    val language: String? = null,
    val types: List<AutocompleteType> = listOf(),
    val components: List<String> = listOf(),
    val strictBounds: Boolean? = null) {

  private constructor(builder: Builder) : this(
      offset = builder.offset,
      location = builder.location,
      radius = builder.radius,
      language = builder.language,
      types = builder.types,
      components = builder.components,
      strictBounds = builder.strictBounds
  )

  companion object {
    fun create(init: Builder.() -> Unit) = Builder(init).build()
    internal fun default() = AutocompleteOptions()
  }

  class Builder private constructor() {

    constructor(init: Builder.() -> Unit) : this() {
      init()
    }

    internal var offset: Int? = null
    internal var location: Pair<Double, Double>? = null
    internal var radius: Int? = null
    internal var language: String? = null
    internal var types: List<AutocompleteType> = listOf()
    internal var components: List<String> = listOf()
    internal var strictBounds: Boolean? = null

    fun offset(getOffset: Builder.() -> Int?) = apply { offset = getOffset() }
    fun location(getLocation: Builder.() -> Pair<Double, Double>?) = apply { location = getLocation() }
    fun radius(getRadius: Builder.() -> Int?) = apply { radius = getRadius() }
    fun language(getLanguage: Builder.() -> String?) = apply { language = getLanguage() }
    fun types(getTypes: Builder.() -> List<AutocompleteType>) = apply { types = getTypes() }
    fun components(getComponents: Builder.() -> List<String>) = apply { components = getComponents() }
    fun strictBounds() = apply { strictBounds = true }

    fun build() = AutocompleteOptions(this)
  }
}