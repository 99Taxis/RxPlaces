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
    val structured_formatting: StructuredFormatting = StructuredFormatting("")
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
    val main_text: String,
    val main_text_matched_substrings: List<MatchedSubstring> = listOf(),
    val secondary_text: String = ""
)

data class PlaceAutocompleteResponse(
    val status: String,
    val predictions: List<Prediction>
)

enum class AutocompleteState {
  QUERYING, SUCCESS, FAILURE
}