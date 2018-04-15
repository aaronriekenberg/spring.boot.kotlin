package org.aaron.springboot.kotlin.config

import mu.KLogging
import org.aaron.springboot.kotlin.model.PlaceNameAndURI
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "weather")
@Component
class WeatherConfiguration {

    companion object : KLogging()

    val placeNameAndUriList = mutableListOf<PlaceNameAndURI>()

    @PostConstruct
    fun postConstruct() {
        logger.info { "placeNameAndUriList = $placeNameAndUriList" }
    }
}