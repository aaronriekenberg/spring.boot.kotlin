package org.aaron.springboot.kotlin.repository

import org.aaron.springboot.kotlin.model.TestObject
import org.aaron.springboot.kotlin.model.TestObjectAndID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

@Service
class TestRepository {

    private val logger: Logger = LoggerFactory.getLogger(TestRepository::class.java)

    private val map: ConcurrentHashMap<Int, TestObject> = ConcurrentHashMap()

    fun createOne(testObjectMono: Mono<TestObject>): Mono<TestObjectAndID> {
        logger.info("in createOne")
        return testObjectMono.flatMap { testObject ->
            logger.info("inside flatMap testObject = ${testObject}")
            var done = false
            var id = 0
            while (!done) {
                id = map.size + 1
                done = (map.putIfAbsent(id, testObject) == null)
                if (done) {
                    logger.info("saved {} id {} new size {}", testObject, id, map.size)
                } else {
                    logger.info("collision saving id {} trying again", id)
                }
            }
            Mono.just(TestObjectAndID(testObject = testObject, id = id))
        }
    }

    fun getOne(id: Int): Mono<TestObject> {
        return Mono.justOrEmpty(map[id])
    }

    fun getAll(): Flux<TestObjectAndID> {
        return Flux.fromIterable(map.entries.map { e -> TestObjectAndID(e.key, e.value) })
    }
}