package org.aaron.springboot.kotlin.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder

data class PlaceNameAndURI(var placeName: String = "", var uri: String = "")

@JsonPropertyOrder("placeName", "statusCode", "title", "temperature")
data class Weather(
        val placeName: String,
        val statusCode: Int,
        val reportTitle: String? = null,
        val reportTemperature: String? = null,
        val reportText: String? = null)