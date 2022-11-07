package de.spotifyvotingqueue.svqbackend.dtos

data class TokenResponseDto(
    val access_token: String,
    val token_type: String,
    val scope: String?,
    val expires_in: Int,
    val refresh_token: String
)
