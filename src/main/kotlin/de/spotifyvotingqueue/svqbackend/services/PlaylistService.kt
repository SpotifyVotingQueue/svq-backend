package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.database.PartyJpaRepository
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
    lateinit var spotifyApi: SpotifyApi

    @Autowired
    lateinit var accessService: AccessTokenService

    @Autowired
    lateinit var queueService: QueueService

    @Autowired
    lateinit var partyJpaRepository: PartyJpaRepository

    fun getTrackById(id: String): Track {
        val token = accessService.getNewestAccessEntity();
        spotifyApi.accessToken = token.accesstoken;
        spotifyApi.refreshToken = token.refreshtoken;
        return spotifyApi
            .getTrack(id)
            .build()
            .execute()
    }

    fun getTracksForPlaylistSimplified(playlistId: String, partyId: String): Paging<PlaylistTrack> {
        val token = accessService.getMatchingToken(partyId);
        spotifyApi.accessToken = token.accesstoken;
        spotifyApi.refreshToken = token.refreshtoken;
        return spotifyApi
            .getPlaylistsItems(playlistId)
            .build()
            .execute()
    }

    fun getFourPlaylists(partyId: String): List<PlaylistSimplified> {
        val token = accessService.getMatchingToken(partyId);
        spotifyApi.accessToken = token.accesstoken;
        spotifyApi.refreshToken = token.refreshtoken;
        return spotifyApi.listOfCurrentUsersPlaylists
            .build()
            .execute()
            .items
            .toList()
            .take(4);
    }

    fun addPlaylistToQueue(id: String, playlistId: String) {
        val party = partyJpaRepository.findByCode(id) ?: throw Exception("Party not found");
        getTracksForPlaylistSimplified(playlistId, id).items.forEach {
            queueService.addTrackToQueue(party, it.track.id);
        }
    }
}