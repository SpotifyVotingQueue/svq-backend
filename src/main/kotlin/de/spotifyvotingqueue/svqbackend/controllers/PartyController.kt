package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.database.model.PartyEntity
import de.spotifyvotingqueue.svqbackend.database.PartyJpaRepository
import de.spotifyvotingqueue.svqbackend.dtos.PartyCreatedDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
@RequestMapping("/api/v1/party")
class PartyController @Autowired constructor(
    val partyJpaRepository: PartyJpaRepository,
) {

    @PostMapping
    fun create(@RegisteredOAuth2AuthorizedClient("spotify") authorizedClient: OAuth2AuthorizedClient): PartyCreatedDto {
        val party = PartyEntity(
            code = generatePartyCode(),
            hostAccessToken = authorizedClient.accessToken.tokenValue,
            hostRefreshToken = authorizedClient.refreshToken!!.tokenValue,
        )
        val createdParty = partyJpaRepository.save(party);
        return PartyCreatedDto(createdParty.code)
    }
}

const val MIN_PARTY_CODE_VALUE = 100_000
const val MAX_PARTY_CODE_VALUE = 999_999

fun generatePartyCode() = "%06d".format(Random.Default.nextInt(MIN_PARTY_CODE_VALUE, MAX_PARTY_CODE_VALUE))
