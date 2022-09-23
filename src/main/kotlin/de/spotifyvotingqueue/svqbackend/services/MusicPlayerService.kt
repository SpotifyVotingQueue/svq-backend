package de.spotifyvotingqueue.svqbackend.services

import com.google.gson.JsonArray
import de.spotifyvotingqueue.svqbackend.adapters.SpotifyApiBuilder
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.model_objects.specification.Track

@Service
class MusicPlayerService {

    fun addTrackToQueue(trackId: String) {
        SpotifyApiBuilder.getInstance().spotifyApi
            .addItemToUsersPlaybackQueue(trackId)
            .build()
            .execute();
    }

    fun playTrack(track: Track) {
        var uris = JsonArray().apply {
            add(track.uri)
        };
        SpotifyApiBuilder.getInstance().spotifyApi
            .startResumeUsersPlayback()
            .uris(uris)
            .build()
            .execute();
    }

    fun skipCurrentTrack() {
        SpotifyApiBuilder.getInstance().spotifyApi
            .skipUsersPlaybackToNextTrack()
            .build()
            .execute();
    }
}