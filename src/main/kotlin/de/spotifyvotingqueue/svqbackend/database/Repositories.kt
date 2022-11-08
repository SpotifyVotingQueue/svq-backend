package de.spotifyvotingqueue.svqbackend.database

import de.spotifyvotingqueue.svqbackend.database.model.AccessEntity
import de.spotifyvotingqueue.svqbackend.database.model.PartyEntity
import de.spotifyvotingqueue.svqbackend.database.model.QueueTrack
import de.spotifyvotingqueue.svqbackend.database.model.RedirectEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PartyJpaRepository : JpaRepository<PartyEntity, UUID>

interface QueueTrackJpaRepository : JpaRepository<QueueTrack, UUID>

interface AccessJpaRepository : JpaRepository<AccessEntity, UUID> {
    fun findFirstByOrderByCreatedDesc(): AccessEntity?
}

interface RedirectJpaRepository : JpaRepository<RedirectEntity, UUID> {
    fun findBySession(session: String): RedirectEntity?
}