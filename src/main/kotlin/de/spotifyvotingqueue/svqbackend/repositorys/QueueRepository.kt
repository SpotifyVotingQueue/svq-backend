package de.spotifyvotingqueue.svqbackend.repositorys

import de.spotifyvotingqueue.svqbackend.dto.Queue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QueueRepository : JpaRepository<Queue, Long> {
    fun findByParty(party: Long): Queue?;

}
