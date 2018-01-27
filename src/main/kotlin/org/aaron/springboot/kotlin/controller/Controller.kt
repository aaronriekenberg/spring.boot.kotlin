package org.aaron.springboot.kotlin.controller

import org.aaron.springboot.kotlin.model.TestObject
import org.aaron.springboot.kotlin.model.TestObjectAndID
import org.aaron.springboot.kotlin.repository.TestRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Service
class Controller(@Autowired private val testRepository: TestRepository) {

    private val logger: Logger = LoggerFactory.getLogger(Controller::class.java)

    fun createOne(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in createOne request = {}", request);

        val testObjectMono = request.bodyToMono(TestObject::class.java)

        return testRepository.createOne(testObjectMono).flatMap {
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(it))
        }
    }

    fun getOne(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in getOne request = {}", request);

        val id = request.pathVariable("id").toInt()

        return testRepository.getOne(id)
                .flatMap { testObject ->
                    ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromObject(testObject))
                }
                .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun getAll(request: ServerRequest): Mono<ServerResponse> {
        logger.info("in getAll request = {}", request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(testRepository.getAll(), TestObjectAndID::class.java)
    }

}