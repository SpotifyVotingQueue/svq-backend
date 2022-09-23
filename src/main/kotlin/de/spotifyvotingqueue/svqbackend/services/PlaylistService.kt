package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.adapters.SpotifyApiBuilder
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.Track
import java.util.*

@Service
class PlaylistService {

    fun getUsers4FavoritePartyPlaylists(): List<PlaylistSimplified> {
        val playlists = SpotifyApiBuilder.getInstance().spotifyApi
            .listOfCurrentUsersPlaylists
            .build()
            .execute()
            .items
            .asList();

        return playlists
            .filter { this.filterPartyPlaylistName(it.name) }
            .subList(0, 3);
    }

    private fun filterPartyPlaylistName(name: String) : Boolean {
        val partyPlaylistNames = listOf("feiern", "party", "suff", "saufen", "treffen", "malle");
        partyPlaylistNames.forEach() {
            if (name.lowercase().contains(it)) {
                return true;
            }
        }
        return false;
    }

    fun getTrackById(id: String): Track {
        return SpotifyApiBuilder.getInstance().spotifyApi
            .getTrack(id)
            .build()
            .execute();
    }

    fun getTracksForPlaylistSimplified(playlistSimplified: PlaylistSimplified): Paging<PlaylistTrack> {
        return SpotifyApiBuilder.getInstance().spotifyApi
            .getPlaylistsItems(playlistSimplified.id)
            .build()
            .execute();
    }
}