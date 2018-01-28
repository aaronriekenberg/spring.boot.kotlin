package org.aaron.springboot.kotlin.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TestHealthIndicator : ReactiveHealthIndicator {

    override fun health(): Mono<Health> {
        return Mono.just(Health.Builder().up().build());
    }

}