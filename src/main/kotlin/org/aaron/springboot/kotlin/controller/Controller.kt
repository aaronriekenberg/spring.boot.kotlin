package org.aaron.springboot.kotlin.controller

import org.aaron.springboot.kotlin.model.TestResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class Controller {

    private val logger: Logger = LoggerFactory.getLogger(Controller::class.java)

    fun getOne(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in getOne request = {}", request);

        val id = request.pathVariable("id")

        val response = TestResponse(id = id, message = "hello ${id}")

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(response))
    }

    fun getAll(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in getAll request = {}", request);

        val flux = Flux.fromIterable((0..10).map { TestResponse(id = "id", message = "hello ${it}") })

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(flux, TestResponse::class.java)
    }

}