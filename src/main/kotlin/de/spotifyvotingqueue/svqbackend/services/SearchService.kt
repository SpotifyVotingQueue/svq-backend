package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.adapters.SpotifyApiBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.Track
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest

@Service
class SearchService {

        fun searchForSong(songName: String): List<Track>  {
                return SpotifyApiBuilder.spotifyApi
                        .searchTracks(songName)
                        .build()
                        .execute()
                        .items
                        .asList();
        }

        fun searchForPlaylistSimplified(playlistName: String): List<PlaylistSimplified> {
                return SpotifyApiBuilder.spotifyApi
                        .searchPlaylists(playlistName)
                        .build()
                        .execute()
                        .items
                        .asList();
        }

        fun getTrackById(id: String): Track {
                return SpotifyApiBuilder.spotifyApi
                        .getTrack(id)
                        .build()
                        .execute();
        }

        fun getTracksForPlaylistSimplified(playlistSimplified: PlaylistSimplified): Paging<PlaylistTrack> {
                return SpotifyApiBuilder.spotifyApi
                        .getPlaylistsItems(playlistSimplified.id)
                        .build()
                        .execute();
        }
}