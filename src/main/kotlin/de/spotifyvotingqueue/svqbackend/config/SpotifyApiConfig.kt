package de.spotifyvotingqueue.svqbackend.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.web.context.annotation.RequestScope
import se.michaelthelin.spotify.SpotifyApi


@Configuration
class SpotifyApiConfig {

    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}")
    private val clientId: String? = null;

    @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}")
    private val clientSecret: String? = null;

    @Bean
    fun getSpotifyApi() : SpotifyApi {
        return SpotifyApi.builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    }


}