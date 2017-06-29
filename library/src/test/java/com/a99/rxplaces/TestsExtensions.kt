package com.a99.rxplaces

import com.google.gson.Gson
import rx.Observable

internal fun getFakeResponse(): GeocodeResponse {
  val json: String = """
{
   "results":[
      {
         "address_components":[
            {
               "long_name":"254",
               "short_name":"254",
               "types":[
                  "street_number"
               ]
            },
            {
               "long_name":"Rua Joaquim Floriano",
               "short_name":"R. Joaquim Floriano",
               "types":[
                  "route"
               ]
            },
            {
               "long_name":"Itaim Bibi",
               "short_name":"Itaim Bibi",
               "types":[
                  "political",
                  "sublocality",
                  "sublocality_level_1"
               ]
            },
            {
               "long_name":"São Paulo",
               "short_name":"São Paulo",
               "types":[
                  "administrative_area_level_2",
                  "political"
               ]
            },
            {
               "long_name":"São Paulo",
               "short_name":"SP",
               "types":[
                  "administrative_area_level_1",
                  "political"
               ]
            },
            {
               "long_name":"Brasil",
               "short_name":"BR",
               "types":[
                  "country",
                  "political"
               ]
            },
            {
               "long_name":"04534-001",
               "short_name":"04534-001",
               "types":[
                  "postal_code"
               ]
            }
         ],
         "formatted_address":"R. Joaquim Floriano, 254 - Itaim Bibi, São Paulo - SP, 04534-001, Brasil",
         "geometry":{
            "location":{
               "lat":-23.5838378,
               "lng":-46.6726088
            },
            "location_type":"APPROXIMATE",
            "viewport":{
               "northeast":{
                  "lat":-23.58248881970849,
                  "lng":-46.6712598197085
               },
               "southwest":{
                  "lat":-23.5851867802915,
                  "lng":-46.6739577802915
               }
            }
         },
         "place_id":"ChIJWRNvGF9XzpQRetBgux1t6qw",
         "types":[
            "establishment",
            "food",
            "point_of_interest",
            "restaurant"
         ]
      }
   ],
   "status":"OK"
}
"""
  return Gson().fromJson(json, GeocodeResponse::class.java)
}

fun simulateTyping(words: Observable<String>): Observable<String> {
  return words.map { it.toCharArray() }
      .concatMap { charArray ->
        Observable.from(charArray.toList())
            .map { it.toString() }
            .scan { accumulator: String, next: String -> accumulator + next }
      }
}
