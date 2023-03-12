package de.spotifyvotingqueue.svqbackend.dtos

data class StateDto(
    val queueChanged: Boolean,
    val songChanged: Boolean
)
