package com.a99.rxplaces

import com.google.gson.annotations.SerializedName

data class Prediction(
    val id: String,
    val description: String,
    @SerializedName("place_id")
    val placeId: String,
    val reference: String,
    val terms: List<Term> = listOf(),
    val types: List<String> = listOf(),
    @SerializedName("matched_substrings")
    val matchedSubstrings: List<MatchedSubstring> = listOf(),
    @SerializedName("structured_formatting")
    val structuredFormatting: StructuredFormatting = StructuredFormatting("")
)

data class Term(
    val value: String,
    val offset: Int
)

data class MatchedSubstring(
    val offset: Int,
    val length: Int
)

data class StructuredFormatting(
    @SerializedName("main_text") val mainText: String,
    @SerializedName("main_text_matched_substrings")
    val mainTextMatchedSubstrings: List<MatchedSubstring> = listOf(),
    @SerializedName("secondary_text") val secondaryText: String = ""
)

internal data class PlaceAutocompleteResponse(
    val status: String,
    val predictions: List<Prediction>
)

enum class AutocompleteState {
  QUERYING, SUCCESS, FAILURE
}