package org.aaron.springboot.kotlin.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "message")
data class TestResponse(val id: String, val message: String)