package de.spotifyvotingqueue.svqbackend.database

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "party")
class PartyEntity(
    @GeneratedValue @Id var partyId: UUID? = null,
    val hostAccessToken: String,
    val hostRefreshToken: String,
)
