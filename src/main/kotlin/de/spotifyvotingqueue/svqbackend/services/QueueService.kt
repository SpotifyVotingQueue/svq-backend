package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.database.PartyJpaRepository
import de.spotifyvotingqueue.svqbackend.database.QueueTrackJpaRepository
import de.spotifyvotingqueue.svqbackend.database.model.PartyEntity
import de.spotifyvotingqueue.svqbackend.database.model.QueueTrack
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Autowired
    private lateinit var searchService: SearchService;

    fun addTrackToQueue(party: PartyEntity, trackId: String): Boolean {
        val track = QueueTrack(trackId, party);
        if (!party.addTrack(track)) {
            return false;
        }
        partyJpaRepository.save(party);
        queueRepository.save(track);
        return true;
    }

    fun upvoteTrack(party: PartyEntity, trackId: String) {
        val track = party.getTrack(trackId)
        track?.upvote()
    }

    fun downvoteTrack(party: PartyEntity, trackId: String) {
        val track = party.getTrack(trackId)
        track?.downvote()
    }

    fun getQueue(party: PartyEntity, withoutLocked: Boolean): List<QueueTrack> {
        val queue = queueRepository.findAllByPartyEntityCode(party.code);
        return if(withoutLocked) queue.filter { !it.locked }.sortedBy { it.getScore() } else queue.sortedBy { it.locked; it.getScore() }
    }

    @Scheduled(cron = "*/10 * * * * *")
    private fun syncRemoteQueueForAllParties() {
        partyJpaRepository
            .findAll()
            .forEach(this::syncRemoteQueue);
    }
    @Transactional
    private fun syncRemoteQueue(party: PartyEntity)
    {
        val user = accessService.getMatchingToken(party.code);
        val remoteQueue =  musicPlayerService.getUsersQueue(user);
        if(remoteQueue.isEmpty() && party.queueTracks.isNotEmpty()) {
            val track = getNextTrack(party);
            musicPlayerService.addTrackToQueue(user, searchService.getSong(track.trackId).uri);
            track.locked = true;
            queueRepository.save(track);
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

    fun getLockedTrack(party: PartyEntity): QueueTrack? {
        return queueRepository.findByLocked(true).firstOrNull();
    }
}