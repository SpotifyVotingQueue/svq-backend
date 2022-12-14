package de.spotifyvotingqueue.svqbackend.dtos

data class PlaylistDto(
    val name: String,
    val owner: String,
    val numberTracks: Int,
    val cover: Href
)
