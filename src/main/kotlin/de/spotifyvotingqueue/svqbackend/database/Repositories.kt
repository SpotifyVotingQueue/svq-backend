package de.spotifyvotingqueue.svqbackend.database

import de.spotifyvotingqueue.svqbackend.database.model.PartyEntity
import de.spotifyvotingqueue.svqbackend.database.model.QueueTrack
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PartyJpaRepository : JpaRepository<PartyEntity, UUID>

interface QueueTrackJpaRepository : JpaRepository<QueueTrack, UUID>
