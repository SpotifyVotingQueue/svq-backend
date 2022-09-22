package de.spotifyvotingqueue.svqbackend.repositorys

import de.spotifyvotingqueue.svqbackend.dto.QueueTrack
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QueueTrackRepository : JpaRepository<QueueTrack, Long> {
}