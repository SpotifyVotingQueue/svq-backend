package de.spotifyvotingqueue.svqbackend.models

class Track(id: String,
            val artists: List<Artist>,
            val duration_ms: Int,
            val href: String,
            val is_playable: Boolean,
            val name: String,
            val popularity: Int,
            val preview_url: String,
            val uri: String,
            val is_local: Boolean): SpotifyItem(id) {
}