package org.aaron.springboot.kotlin.service

import org.aaron.springboot.kotlin.config.WeatherConfiguration
import org.aaron.springboot.kotlin.model.Weather
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@Service
class WeatherService(
        @Autowired private val webClient: WebClient,
        @Autowired private val weatherConfiguration: WeatherConfiguration) {

    val logger: Logger = LoggerFactory.getLogger(WeatherService::class.java)

    fun getWeather(): Flux<Weather> {

        var flux = Flux.empty<Weather>()

        weatherConfiguration.placeNameAndUriList.forEach { placeNameAndURI ->
            val uri = URI(placeNameAndURI.uri)

            flux = flux.mergeWith(
                    webClient.get()
                            .uri(uri)
                            .exchange()
                            .flatMap { clientResponse ->
                                if (!clientResponse.statusCode().is2xxSuccessful) {
                                    Mono.just(Weather(
                                            placeName = placeNameAndURI.placeName,
                                            statusCode = clientResponse.statusCode().value()))
                                } else {
                                    clientResponse.toEntity(Map::class.java)
                                            .flatMap { responseEntity ->
                                                val query = responseEntity.body?.get("query") as Map<*, *>?
                                                val results = query?.get("results") as Map<*, *>?
                                                val channel = results?.get("channel") as Map<*, *>?
                                                val item = channel?.get("item") as Map<*, *>?
                                                val title = item?.get("title") as String?
                                                val condition = item?.get("condition") as Map<*, *>?
                                                val temperature = condition?.get("temp") as String?
                                                val text = condition?.get("text") as String?
                                                Mono.just(
                                                        Weather(placeName = placeNameAndURI.placeName,
                                                                statusCode = responseEntity.statusCodeValue,
                                                                reportTitle = title,
                                                                reportTemperature = temperature,
                                                                reportText = text))

                                            }
                                }
                            })
        }

        return flux
    }
}