package de.spotifyvotingqueue.svqbackend.services

import com.google.gson.JsonArray
import de.spotifyvotingqueue.svqbackend.database.model.AccessEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.miscellaneous.Device
import se.michaelthelin.spotify.model_objects.specification.Track

@Service
class MusicPlayerService {

    @Autowired
    lateinit var spotifyApi: SpotifyApi

    @Autowired
    lateinit var accessService: AccessTokenService

    fun addTrackToQueue(user: AccessEntity, trackId: String) {
        val devices = spotifyApi
            .usersAvailableDevices
            .build()
            .execute()

        spotifyApi.accessToken = user.accesstoken
        spotifyApi.refreshToken = user.refreshtoken
        spotifyApi
            .addItemToUsersPlaybackQueue(trackId)
            .device_id(devices.filter { it.is_active || it.type.equals("Smartphone") || it.type.equals("Computer") }.sortedBy { it.is_active }.first().id)
            .build()
            .execute()
    }

    fun getUsersQueue(user: AccessEntity): List<Track> {
        spotifyApi.accessToken = user.accesstoken
        spotifyApi.refreshToken = user.refreshtoken
        return spotifyApi
            .theUsersQueue
            .build()
            .execute()
            .queue;
    }

    fun playTrack(track: Track) {
        val uris = JsonArray().apply {
            add(track.uri)
        };
        val token = accessService.getNewestAccessEntity();
        spotifyApi.accessToken = token.accesstoken;
        spotifyApi.refreshToken = token.refreshtoken;
        spotifyApi
            .startResumeUsersPlayback()
            .uris(uris)
            .build()
            .execute()
    }

    fun skipCurrentTrack() {
        val token = accessService.getNewestAccessEntity();
        spotifyApi.accessToken = token.accesstoken;
        spotifyApi.refreshToken = token.refreshtoken;
        spotifyApi
            .skipUsersPlaybackToNextTrack()
            .build()
            .execute()
    }
}