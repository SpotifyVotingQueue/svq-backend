package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.dto.Queue
import de.spotifyvotingqueue.svqbackend.dto.QueueTrack
import de.spotifyvotingqueue.svqbackend.exceptions.PartyNotFoundException
import de.spotifyvotingqueue.svqbackend.repositorys.QueueRepository
import de.spotifyvotingqueue.svqbackend.repositorys.QueueTrackRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.model_objects.specification.Track

@Service
class QueueService {

    @Autowired
    lateinit var queueRepository: QueueRepository

    @Autowired
    lateinit var queueTrackRepository: QueueTrackRepository

    fun getQueue(party: Long): List<QueueTrack> {
        return queueRepository.findByParty(party)?.getQueue() ?: emptyList();
    }

    fun addTrackToQueue(trackId: String, party: Long): QueueTrack {
        queueRepository.findByParty(party)?.let {
            val queueTrack = QueueTrack(trackId)
            queueTrackRepository.save(queueTrack)
            return queueTrack
        } ?: throw PartyNotFoundException()
    }

}