package org.aaron.springboot.kotlin.service

import mu.KLogging
import org.aaron.springboot.kotlin.model.TestObject
import org.aaron.springboot.kotlin.model.TestObjectAndID
import org.aaron.springboot.kotlin.repository.TestRepository
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

    companion object : KLogging()

    fun createOne(testObjectMono: Mono<TestObject>): Mono<TestObjectAndID> {
        logger.debug("createOne")

        return testRepository.createOne(testObjectMono)
    }

    fun getOne(id: Int): Mono<TestObject> {
        logger.debug("getOne")

        return testRepository.getOne(id)
    }

    fun getAll(): Flux<TestObjectAndID> {
        logger.debug("getAll")

        return testRepository.getAll()
    }

    fun getProxy(): Mono<ResponseEntity<String>> {
        logger.debug("getProxy")

        val uri = URI(proxyURI)

        return webClient.get()
                .uri(uri)
                .exchange()
                .flatMap { it.toEntity<String>() }
    }

}