package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.database.PartyJpaRepository
import de.spotifyvotingqueue.svqbackend.database.QueueTrackJpaRepository
import de.spotifyvotingqueue.svqbackend.database.model.AccessEntity
import de.spotifyvotingqueue.svqbackend.database.model.PartyEntity
import de.spotifyvotingqueue.svqbackend.database.model.QueueTrack
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class QueueService {

    @Autowired
    private lateinit var queueRepository: QueueTrackJpaRepository

    @Autowired
    private lateinit var partyJpaRepository: PartyJpaRepository;

    @Autowired
    private lateinit var musicPlayerService: MusicPlayerService;

    @Autowired
    private lateinit var accessService: AccessTokenService;

    fun addTrackToQueue(party: PartyEntity, trackId: String) {
        party.addTrack(trackId);
        partyJpaRepository.save(party);
    }

    fun upvoteTrack(party: PartyEntity, trackId: String) {
        val track = party.getTrack(trackId)
        track?.upvote()
    }

    fun downvoteTrack(party: PartyEntity, trackId: String) {
        val track = party.getTrack(trackId)
        track?.downvote()
    }

    fun getQueue(party: PartyEntity): List<QueueTrack> {
        return party.queueTracks.sortedBy { it.getScore() }
    }

    @Scheduled(cron = "*/10 * * * * *")
    private fun syncRemoteQueueForAllParties() {
        partyJpaRepository
            .findAll()
            .forEach(this::syncRemoteQueue);
    }

    private fun syncRemoteQueue(party: PartyEntity)
    {
        val user = accessService.getMatchingToken(party.code);
        val remoteQueue =  musicPlayerService.getUsersQueue(user);
        if(remoteQueue.isEmpty()) { //TODO warum nur wenn empty?
            musicPlayerService.addTrackToQueue(user, getNextTrack(party).trackId);
        }
    }

    private fun getNextTrack(party: PartyEntity): QueueTrack {
        var highestScore = 0
        var highestScoreTrack = party.queueTracks.first()
        for (track in party.queueTracks) {
            if (track.getScore() > highestScore) {
                highestScore = track.getScore()
                highestScoreTrack = track
            }
        }
        party.removeTrack(highestScoreTrack); //TODO der muss noch in eine art "locked" Zustand gespeichert werden
        return highestScoreTrack
    }
}