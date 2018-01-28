package org.aaron.springboot.kotlin.controller

import mu.KLogging
import org.aaron.springboot.kotlin.model.TestObject
import org.aaron.springboot.kotlin.service.TestService
import org.aaron.springboot.kotlin.service.WeatherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Service
class Controller(
        @Autowired private val testService: TestService,
        @Autowired private val weatherService: WeatherService) {

    companion object : KLogging()

    fun createOne(request: ServerRequest): Mono<ServerResponse> {
        logger.debug { "in createOne request = $request" }

        val testObjectMono = request.bodyToMono<TestObject>()

        return testService.createOne(testObjectMono).flatMap {
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .syncBody(it)
        }
    }

    fun getOne(request: ServerRequest): Mono<ServerResponse> {
        logger.debug { "in getOne request = $request" }

        val id = request.pathVariable("id").toInt()

        return testService.getOne(id)
                .flatMap {
                    ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .syncBody(it)
                }
                .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun getAll(request: ServerRequest): Mono<ServerResponse> {
        logger.debug { "in getAll request = $request" }

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(testService.getAll())
    }

    fun getProxy(request: ServerRequest): Mono<ServerResponse> {
        logger.debug { "in getProxy request = $request" }

        val result = testService.getProxy()

        return result.flatMap {
            val contentType = it.headers.contentType ?: MediaType.TEXT_PLAIN
            val body = it.body ?: "null"
            ServerResponse.status(it.statusCode)
                    .contentType(contentType)
                    .syncBody(body)
        }
    }

    fun getWeather(request: ServerRequest): Mono<ServerResponse> {
        logger.debug { "in getWeather request = $request" }

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(weatherService.getWeather())
    }

}