package de.spotifyvotingqueue.svqbackend.adapters

import org.springframework.beans.factory.annotation.Value
import se.michaelthelin.spotify.SpotifyApi

class SpotifyApiBuilder {

    companion object {
        @Value("${spring.security.oauth2.client.registration.spotify.client_id}")
        private val client_id: String? = null;

        @Value("${spring.security.oauth2.client.registration.spotify.client_secret}")
        private val client_secret: String? = null;

        public val spotifyApi = SpotifyApi.Builder()
            .setClientId(client_id)
            .setClientSecret(client_secret)
            .build();
    }
}