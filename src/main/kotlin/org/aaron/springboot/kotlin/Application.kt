package org.aaron.springboot.kotlin

import org.aaron.springboot.kotlin.controller.Controller
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@SpringBootApplication
open class Application {

    @Bean
    open fun routes(controller: Controller): RouterFunction<ServerResponse> {
        return router {
            (path("/kotlin_service/v1") and accept(MediaType.APPLICATION_JSON)).nest {
                GET("/", controller::getAll)
            }
        }
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}