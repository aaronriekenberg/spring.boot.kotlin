package org.aaron.springboot.kotlin.config

import org.aaron.springboot.kotlin.model.PlaceNameAndURI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "weather")
@Component
class WeatherConfiguration {

    private val logger: Logger = LoggerFactory.getLogger(WeatherConfiguration::class.java)

    val placeNameAndUriList = ArrayList<PlaceNameAndURI>()

    @PostConstruct
    fun postConstruct() {
        logger.info("placeNameAndUriList = ${placeNameAndUriList}")
    }
}