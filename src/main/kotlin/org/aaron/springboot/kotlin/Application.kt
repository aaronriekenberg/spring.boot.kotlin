package org.aaron.springboot.kotlin

import org.aaron.springboot.kotlin.controller.Controller
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@SpringBootApplication
class Application {

    @Bean
    fun routes(controller: Controller): RouterFunction<ServerResponse> {
        return router {
            (path("/kotlin_service/v1") and accept(MediaType.APPLICATION_JSON)).nest {
                (POST("/") and contentType(MediaType.APPLICATION_JSON)).invoke(controller::createOne)
                GET("/weather", controller::getWeather)
                GET("/proxy", controller::getProxy)
                GET("/{id}", controller::getOne)
                GET("/", controller::getAll)
            }
        }
    }

    @Bean
    fun webClient(): WebClient {
        return WebClient.create()
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}