package de.spotifyvotingqueue.svqbackend.models

class Album(id: String,
            val album_type: String,
            val total_tracks: Int,
            val available_markets: List<String>,
            val href: String,
            val images: List<Image>,
            val name: String,
            val release_date: String,
            val release_date_precision: String,
            val uri: String,
            val artists: List<Artist>,
            val track: List<SpotifyItem>) : SpotifyItem(id) {


}