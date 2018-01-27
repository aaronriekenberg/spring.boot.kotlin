package org.aaron.springboot.kotlin.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder

data class TestObject(val data: String)

@JsonPropertyOrder("id", "testObject")
data class TestObjectAndID(val id: Int, val testObject: TestObject)