package de.spotifyvotingqueue.svqbackend.dtos

data class TrackDto(
    val id: String,
    val name: String,
    val artists: List<String>,
    val coverUrl: String,
) {
}