package org.aaron.springboot.kotlin.repository

import mu.KLogging
import org.aaron.springboot.kotlin.model.TestObject
import org.aaron.springboot.kotlin.model.TestObjectAndID
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import java.util.concurrent.ConcurrentHashMap

@Service
class TestRepository {

    companion object : KLogging()

    private val map: ConcurrentHashMap<Int, TestObject> = ConcurrentHashMap()

    fun createOne(testObjectMono: Mono<TestObject>): Mono<TestObjectAndID> {
        return testObjectMono.flatMap { testObject ->
            var done = false
            var id = 0
            while (!done) {
                id = map.size + 1
                done = (map.putIfAbsent(id, testObject) == null)
                if (done) {
                    logger.info { "saved $testObject id $id new size ${map.size}" }
                } else {
                    logger.info { "collision saving id $id trying again" }
                }
            }
            TestObjectAndID(testObject = testObject, id = id).toMono()
        }
    }

    fun getOne(id: Int): Mono<TestObject> {
        return Mono.justOrEmpty(map[id])
    }

    fun getAll(): Flux<TestObjectAndID> {
        return map.entries.map { e -> TestObjectAndID(e.key, e.value) }.toFlux()
    }
}