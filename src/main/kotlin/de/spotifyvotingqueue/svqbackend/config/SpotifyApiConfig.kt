package de.spotifyvotingqueue.svqbackend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import se.michaelthelin.spotify.SpotifyApi

@Configuration
class SpotifyApiConfig {

    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}")
    private val clientId: String? = null;

    @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}")
    private val clientSecret: String? = null;

    @Bean
    fun getSpotifyApi() : SpotifyApi {
        return SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    }

}