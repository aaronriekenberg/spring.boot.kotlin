package org.aaron.springboot.kotlin.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Component
class TestHealthIndicator : ReactiveHealthIndicator {

    override fun health(): Mono<Health> {
        return Health.Builder().up().build().toMono()
    }

}