package org.aaron.springboot.kotlin.controller

import org.aaron.springboot.kotlin.model.TestResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("kotlin_service/v1")
class Controller {

    private val logger: Logger = LoggerFactory.getLogger(Controller::class.java)

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): ResponseEntity<TestResponse> {
        logger.info("in get id = '{}'", id);

        return ResponseEntity.ok(TestResponse(id = id, message = "hello"))
    }

}