package de.spotifyvotingqueue.svqbackend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import se.michaelthelin.spotify.SpotifyApi

@Configuration
class SpotifyApiConfig {

    @Value("\${spring.security.oauth2.client.registration.spotify.client_id}")
    private val client_id: String? = null;

    @Value("\${spring.security.oauth2.client.registration.spotify.client_secret}")
    private val client_secret: String? = null;

    @Bean
    public fun getSpotifyApi() : SpotifyApi {
        return SpotifyApi.Builder()
            .setClientId(client_id)
            .setClientSecret(client_secret)
            .build();
    }
}