package org.aaron.springboot.kotlin.controller

import org.aaron.springboot.kotlin.model.TestObject
import org.aaron.springboot.kotlin.service.TestService
import org.aaron.springboot.kotlin.service.WeatherService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Service
class Controller(
        @Autowired private val testService: TestService,
        @Autowired private val weatherService: WeatherService) {

    private val logger: Logger = LoggerFactory.getLogger(Controller::class.java)

    fun createOne(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in createOne request = {}", request);

        val testObjectMono = request.bodyToMono<TestObject>()

        return testService.createOne(testObjectMono).flatMap {
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .syncBody(it)
        }
    }

    fun getOne(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in getOne request = {}", request);

        val id = request.pathVariable("id").toInt()

        return testService.getOne(id)
                .flatMap {
                    ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromObject(it))
                }
                .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun getAll(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in getAll request = {}", request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(testService.getAll())
    }

    fun getProxy(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in getProxy request = {}", request);

        val result = testService.getProxy()

        return result.flatMap {
            ServerResponse.status(it.statusCode)
                    .contentType(it.headers.contentType ?: MediaType.TEXT_PLAIN)
                    .syncBody(it.body ?: "null")
        }
    }

    fun getWeather(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in getWeather request = {}", request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(weatherService.getWeather())
    }

}