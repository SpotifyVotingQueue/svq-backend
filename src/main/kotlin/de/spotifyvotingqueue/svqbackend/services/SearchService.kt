package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.database.AccessJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.Track

@Service
class SearchService {

        private val logger = org.slf4j.LoggerFactory.getLogger(SearchService::class.java);

        @Autowired
        lateinit var spotifyApi: SpotifyApi;

        @Autowired
        lateinit var accessService: AccessTokenService;

        fun searchForSong(songName: String, partyId: String): List<Track>  {
                logger.info("Client ID: " + spotifyApi.clientId);
                logger.info("Client Secret: " + spotifyApi.clientSecret);
                val token = accessService.getMatchingToken(partyId);
                spotifyApi.accessToken = token.accesstoken;
                spotifyApi.refreshToken = token.refreshtoken;
                return spotifyApi
                        .searchTracks(songName)
                        .build()
                        .execute()
                        .items
                        .asList();
        }

        fun getSong(songId: String): Track {
                return spotifyApi
                        .getTrack(songId)
                        .build()
                        .execute();
        }

        fun searchForPlaylistSimplified(playlistName: String): List<PlaylistSimplified> {
                val token = accessService.getNewestAccessEntity();
                spotifyApi.accessToken = token.accesstoken;
                spotifyApi.refreshToken = token.refreshtoken;
                return spotifyApi
                        .searchPlaylists(playlistName)
                        .build()
                        .execute()
                        .items
                        .asList();
        }

        fun getUsersFavoriteSongs(): List<Track> {
                val token = accessService.getNewestAccessEntity();
                spotifyApi.accessToken = token.accesstoken;
                spotifyApi.refreshToken = token.refreshtoken;
                return spotifyApi
                        .usersTopTracks
                        .build()
                        .execute()
                        .items
                        .asList()
        }
}