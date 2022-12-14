package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.database.model.PartyEntity
import de.spotifyvotingqueue.svqbackend.database.PartyJpaRepository
import de.spotifyvotingqueue.svqbackend.database.model.QueueTrack
import de.spotifyvotingqueue.svqbackend.dtos.PartyCreatedDto
import de.spotifyvotingqueue.svqbackend.dtos.TrackDto
import de.spotifyvotingqueue.svqbackend.services.QueueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
@RequestMapping("/api/v1/party")
class PartyController @Autowired constructor(
    val partyJpaRepository: PartyJpaRepository,
    val queueService: QueueService,
    val trackController: TrackController
) {
    @PostMapping
    fun create(@RequestParam("accesscode") accesscode: String): PartyCreatedDto {
        val party = PartyEntity(
            code = generatePartyCode(),
            hostAccessToken = accesscode,
        )
        val createdParty = partyJpaRepository.save(party)
        return PartyCreatedDto(createdParty.code)
    }

    @PostMapping("/queue/{id}/track/{trackId}")
    fun addTrackToQueue(@PathVariable("id") id: String, @PathVariable("trackId") trackId: String) {
        val party = partyJpaRepository.findByCode(id) ?: throw Exception("Party not found")
        queueService.addTrackToQueue(party, trackId);
    }

    @PostMapping("/queue/{id}/track/{trackId}/upvote")
    fun upvoteTrack(@PathVariable("id") id: String, @PathVariable("trackId") trackId: String) {
        val party = partyJpaRepository.findByCode(id) ?: throw Exception("Party not found")
        queueService.upvoteTrack(party, trackId);
    }

    @PostMapping("/queue/{id}/track/{trackId}/downvote")
    fun downvoteTrack(@PathVariable("id") id: String, @PathVariable("trackId") trackId: String) {
        val party = partyJpaRepository.findByCode(id) ?: throw Exception("Party not found")
        queueService.downvoteTrack(party, trackId);
    }

    @GetMapping("/queue/{id}/tracks")
    fun getQueue(@PathVariable("id") id: String): List<TrackDto> {
        val party = partyJpaRepository.findByCode(id) ?: throw Exception("Party not found")
        return queueService.getQueue(party).map { trackController.getTrack(it.trackId) };
    }
}

const val MIN_PARTY_CODE_VALUE = 100_000
const val MAX_PARTY_CODE_VALUE = 999_999

fun generatePartyCode() = "%06d".format(Random.Default.nextInt(MIN_PARTY_CODE_VALUE, MAX_PARTY_CODE_VALUE))
