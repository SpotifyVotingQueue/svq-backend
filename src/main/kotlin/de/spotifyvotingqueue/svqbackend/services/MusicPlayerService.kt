package de.spotifyvotingqueue.svqbackend.services

import com.google.gson.JsonArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Track

@Service
class MusicPlayerService {

    @Autowired
    lateinit var spotifyApi: SpotifyApi;

    @Autowired
    lateinit var accessService: AccessTokenService;

    fun addTrackToQueue(trackId: String) {
        val token = accessService.getNewestAccessEntity();
        spotifyApi.accessToken = token.access_token;
        spotifyApi.refreshToken = token.refresh_token;
        spotifyApi
            .addItemToUsersPlaybackQueue(trackId)
            .build()
            .execute();
    }

    fun playTrack(track: Track) {
        var uris = JsonArray().apply {
            add(track.uri)
        };
        val token = accessService.getNewestAccessEntity();
        spotifyApi.accessToken = token.access_token;
        spotifyApi.refreshToken = token.refresh_token;
        spotifyApi
            .startResumeUsersPlayback()
            .uris(uris)
            .build()
            .execute();
    }

    fun skipCurrentTrack() {
        val token = accessService.getNewestAccessEntity();
        spotifyApi.accessToken = token.access_token;
        spotifyApi.refreshToken = token.refresh_token;
        spotifyApi
            .skipUsersPlaybackToNextTrack()
            .build()
            .execute();
    }
}