package de.spotifyvotingqueue.svqbackend.database

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PartyJpaRepository : JpaRepository<PartyEntity, UUID>
