package org.aaron.springboot.kotlin.service

import mu.KLogging
import org.aaron.springboot.kotlin.config.WeatherConfiguration
import org.aaron.springboot.kotlin.model.Weather
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.toMono
import java.net.URI

@Service
class WeatherService(
        private val webClient: WebClient,
        private val weatherConfiguration: WeatherConfiguration) {

    companion object : KLogging()

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
                                    Weather(
                                            placeName = placeNameAndURI.placeName,
                                            statusCode = clientResponse.statusCode().value()).toMono()
                                } else {
                                    clientResponse.bodyToMono<Map<*, *>>()
                                            .flatMap { bodyMap ->
                                                val query = bodyMap.get("query") as Map<*, *>?
                                                val results = query?.get("results") as Map<*, *>?
                                                val channel = results?.get("channel") as Map<*, *>?
                                                val item = channel?.get("item") as Map<*, *>?
                                                val title = item?.get("title") as String?
                                                val condition = item?.get("condition") as Map<*, *>?
                                                val temperature = condition?.get("temp") as String?
                                                val text = condition?.get("text") as String?

                                                Weather(placeName = placeNameAndURI.placeName,
                                                        statusCode = clientResponse.statusCode().value(),
                                                        reportTitle = title,
                                                        reportTemperature = temperature,
                                                        reportText = text).toMono()

                                            }
                                }
                            })
        }

        return flux
    }
}