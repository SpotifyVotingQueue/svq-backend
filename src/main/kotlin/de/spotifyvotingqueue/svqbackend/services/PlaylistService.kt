package de.spotifyvotingqueue.svqbackend.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.Track

@Service
class PlaylistService {

    @Autowired
    lateinit var spotifyApi: SpotifyApi;

    @Autowired
    lateinit var accessService: AccessTokenService;

    fun getTrackById(id: String): Track {
        val token = accessService.getNewestAccessEntity();
        spotifyApi.accessToken = token.accesstoken;
        spotifyApi.refreshToken = token.refreshtoken;
        return spotifyApi
            .getTrack(id)
            .build()
            .execute();
    }

    fun getTracksForPlaylistSimplified(playlistSimplified: PlaylistSimplified): Paging<PlaylistTrack> {
        val token = accessService.getNewestAccessEntity();
        spotifyApi.accessToken = token.accesstoken;
        spotifyApi.refreshToken = token.refreshtoken;
        return spotifyApi
            .getPlaylistsItems(playlistSimplified.id)
            .build()
            .execute();
    }
}