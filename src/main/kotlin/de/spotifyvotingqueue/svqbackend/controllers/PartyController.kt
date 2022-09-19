package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.database.PartyEntity
import de.spotifyvotingqueue.svqbackend.database.PartyJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/party")
class PartyController @Autowired constructor(
    val partyJpaRepository: PartyJpaRepository,
) {

    @PostMapping
    fun create(@RegisteredOAuth2AuthorizedClient("spotify") authorizedClient: OAuth2AuthorizedClient) {
        val party = PartyEntity(
            hostAccessToken = authorizedClient.accessToken.tokenValue,
            hostRefreshToken = authorizedClient.refreshToken!!.tokenValue,
        )

        partyJpaRepository.save(party);
    }
}
