package org.aaron.springboot.kotlin.service

import org.aaron.springboot.kotlin.model.TestObject
import org.aaron.springboot.kotlin.model.TestObjectAndID
import org.aaron.springboot.kotlin.repository.TestRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@Service
class TestService(
        @Autowired private val webClient: WebClient,
        @Autowired private val testRepository: TestRepository,
        @Value("\${testService.proxyURI}") private val proxyURI: String) {

    val logger: Logger = LoggerFactory.getLogger(TestService::class.java)

    fun createOne(testObjectMono: Mono<TestObject>): Mono<TestObjectAndID> {
        logger.info("createOne")

        return testRepository.createOne(testObjectMono)
    }

    fun getOne(id: Int): Mono<TestObject> {
        logger.info("getOne")

        return testRepository.getOne(id)
    }

    fun getAll(): Flux<TestObjectAndID> {
        logger.info("getAll")

        return testRepository.getAll()
    }

    fun getProxy(): Mono<ResponseEntity<String>> {
        logger.info("getProxy")

        val uri = URI(proxyURI)

        return webClient.get()
                .uri(uri)
                .exchange()
                .flatMap { it.toEntity<String>() }
    }

}