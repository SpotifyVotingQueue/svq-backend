package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.adapters.SpotifyApiConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.Track

@Service
class SearchService {

        @Autowired
        lateinit var spotifyApi: SpotifyApi;

        fun searchForSong(songName: String): List<Track>  {
                return spotifyApi
                        .searchTracks(songName)
                        .build()
                        .execute()
                        .items
                        .asList();
        }

        fun searchForPlaylistSimplified(playlistName: String): List<PlaylistSimplified> {
                return spotifyApi
                        .searchPlaylists(playlistName)
                        .build()
                        .execute()
                        .items
                        .asList();
        }

        fun getUsersFavoriteSongs(): List<Track> {
                return spotifyApi
                        .usersTopTracks
                        .build()
                        .execute()
                        .items
                        .asList()
        }
}