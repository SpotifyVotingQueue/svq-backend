package de.spotifyvotingqueue.svqbackend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import se.michaelthelin.spotify.SpotifyApi


@Configuration
@EnableScheduling
class SpotifyApiConfig {

    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}")
    private val clientId: String? = null

    @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}")
    private val clientSecret: String? = null

    @Bean
    fun getSpotifyApi() : SpotifyApi {
        return SpotifyApi.builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build()
    }


}